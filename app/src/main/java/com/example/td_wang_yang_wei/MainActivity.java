package com.example.td_wang_yang_wei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implémenter l'interface principale de l'application
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Initialisation de paramètres

    private Button btnOk = null;
    private Button btnSign=null;
    private EditText edtPseudo = null;
    private EditText edtPasse=null;
    private ListeDeUtilisateur listeDeUtilisateur;


    //Alerter pour savoir le processus de la programme et alerter les utilisateurs
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        myToast.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connection les layouts avec des backends
        btnSign=findViewById(R.id.btnSign);
        btnOk=findViewById(R.id.btnOK);
        edtPseudo=findViewById(R.id.edtPseudo);
        edtPasse=findViewById(R.id.edtMotDePasse);

        btnSign.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        edtPseudo.setOnClickListener(this);
        edtPasse.setOnClickListener(this);


        //Le dernier pseudo et mot de passe saisi est automatiquement renseigné dans le champ de saisie
        if(this.getProfiles()!=null) {
            listeDeUtilisateur = getProfiles();

            if (!(listeDeUtilisateur.getUtilisateurs().isEmpty())) {
                edtPseudo.setText((listeDeUtilisateur.getUtilisateurs().get(listeDeUtilisateur.getUtilisateurs().size() - 1)).getPseudo());
                edtPasse.setText((listeDeUtilisateur.getUtilisateurs().get(listeDeUtilisateur.getUtilisateurs().size() - 1)).getMotDePasse());
            } else listeDeUtilisateur = new ListeDeUtilisateur();
        }


        //vérifier l'état de réseau
        //this.verifReseau();
    }

    //detect l'état de network
    public void verifReseau(){
        //obtenir l'objet de connectivityManager
        ConnectivityManager netManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=netManager.getActiveNetworkInfo();
        String statsType;
        if(networkInfo!=null){
            NetworkInfo.State netState = networkInfo.getState();
            if(netState.compareTo(NetworkInfo.State.CONNECTED)==0){
                int netType= networkInfo.getType();
                switch(netType){
                    case ConnectivityManager.TYPE_MOBILE :
                        statsType = "Réseau mobile détecté";alerter(statsType); break;
                    case ConnectivityManager.TYPE_WIFI :
                        statsType = "Réseau wifi détecté";alerter(statsType); break;
                }
                btnOk.setEnabled(true);
            }else {
                btnOk.setEnabled(false);
            }
        }else {
            btnOk.setEnabled(false);
        }

    }

    @Override
    protected void onStart() {

        super.onStart();
        alerter("onStart");

        //Lorsque nous cliquons sur un élément dans les comptes
        // nous revenons à MainActivity à partir de SettingsActivity
        // et affichons le profil sur lequel vous avez cliqué dans le champ de saisie
        if (getIntent().getStringExtra("pseudo") != null) {
            edtPseudo.setText(getIntent().getStringExtra("pseudo"));
            Utilisateur u= listeDeUtilisateur.ChercheUtilisateur(getIntent().getStringExtra("pseudo"));
            edtPasse.setText(u.getMotDePasse());
        }

        //verifiy l'état de réseau
        this.verifReseau();
    }

    /**
     * Quand on appuie sur le bouton OK, on ajoute un nouveau profil non existant
     * Quand on appuie sur le le champ de saisie, on affiche un avertissement
     * @param v
     */
    @Override
    public void onClick(View v) {

        final String pseudo = edtPseudo.getText().toString();
        final String pass=edtPasse.getText().toString();
        switch (v.getId()){

            case R.id.btnSign:
                //éviter le cas d'entrée vide
                if(pseudo.equals("")||pass.equals("")){
                    alerter("le pseudo ou passe manque");
                }else {
                    if(listeDeUtilisateur.VerifierPresence(pseudo)){
                        alerter("le nom est occpué");
                        break;
                    }
                    //on a besoin d'un hash code pour creer un nouveau compte
                    if(listeDeUtilisateur.getUtilisateurs().isEmpty()){
                        alerter("manque de compte nécessaire");
                        break;
                    }
                    requestService requestService = requestServiceFactory.createService(listeDeUtilisateur.getUrl(), requestService.class);

                    Call<ResponseBody> call = requestService.creer(listeDeUtilisateur.getUtilisateurs().get(listeDeUtilisateur.getUtilisateurs().size()-1).getHash(), pseudo, pass);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                alerter("crée!");
                            } else {
                                alerter("insuccès");
                            }
                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });

                }



                break;

            case R.id.btnOK:

                //éviter le cas d'entrée vide
                if(pseudo.equals("")||pass.equals("")){
                    alerter("le pseudo ou passe manque");
                }
                else {
                    //cherchez dans la liste de utilisateurs
                    if(listeDeUtilisateur.VerifierPresence(pseudo)){
                        Utilisateur u=listeDeUtilisateur.ChercheUtilisateur(pseudo);
                        if(u.verifierMotDePasse(pass))
                            ConvertToListe(u.getPseudo(),u.getHash(),listeDeUtilisateur.getUrl());
                         else alerter("Revévifiez votre mot de passe");
                    }else {

                        requestService post_request= requestServiceFactory.createService(listeDeUtilisateur.getUrl(), requestService.class);

                        Call<Contenu> call = post_request.authenticate(pseudo,pass);
                        Log.d("url",""+listeDeUtilisateur.getUrl());

                        call.enqueue(new Callback<Contenu>() {
                            @Override
                            public void onResponse(Call<Contenu> call, Response<Contenu> response) {
                                if(response.isSuccessful()){
                                    Log.d("hhhhh","true");
                                    listeDeUtilisateur.AjouterUtilisateur(new Utilisateur(pseudo,pass,response.body().hash));
                                    sauvegarderUtilisateur(listeDeUtilisateur);
                                    ConvertToListe(pseudo,response.body().hash,listeDeUtilisateur.getUrl());
                                }else alerter("le nom n'est pas ou le mot est incorrect");
                            }

                            @Override
                            public void onFailure(Call<Contenu> call, Throwable t) {
                                alerter("pas de connexion");
                            }
                        });
                    }
                    //entrez un profil existant
                }
            break;



            case R.id.edtPseudo:
                alerter("saisir ton pseudo");
            break;
            case R.id.edtMotDePasse:
                alerter("saisir ton mot de passe");
                break;
         }
    }

    /**
     * entrez un profil existant
     * @param pseudo
     */
    public void ConvertToListe(String pseudo,String hash,String url){
        Intent liste=new Intent(this,ChoixListeActivity.class);
        liste.putExtra("pseudo",pseudo);
        liste.putExtra("hash",hash);
        liste.putExtra("url",url);
        startActivity(liste);
    }




    public ListeDeUtilisateur getProfiles() {
        SharedPreferences preferences = getSharedPreferences("utilisateurs", MODE_PRIVATE);
        GsonBuilder builder=new GsonBuilder();
        Gson gson = builder.create();
        String stringListUtilisateur = preferences.getString("utilisateurs", gson.toJson(new ListeDeUtilisateur()));
        ListeDeUtilisateur LU=gson.fromJson(stringListUtilisateur,ListeDeUtilisateur.class);
        return LU;
    }




    public void sauvegarderUtilisateur(ListeDeUtilisateur l){
        Gson gson=new Gson();
        String fileContents = gson.toJson(l);
        SharedPreferences preferences = getSharedPreferences("utilisateurs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("utilisateurs",fileContents);
        editor.commit();


    }


    /**
     * Créez le menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Entrez un élément dans le menu
     * Dans notre cas, accounts uniquement
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                alerter("Menu Compte");
                Intent account=new Intent(this,SettingsActivity.class);
                startActivity(account);

        return super.onOptionsItemSelected(item);
    }
}
