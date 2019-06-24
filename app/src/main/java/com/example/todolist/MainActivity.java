package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOK = null;
    private EditText edtPseudo = null;
    private EditText edtPassword = null;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private String pseudo;
    private String password;
    private String mHash;
    private Intent toChoixListActivity;
    private String myUrl;
    public Bundle data;
    private Boolean connexion;
    private DataProvider dataProvider;
    private List<ItemToDo> listItemModifie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOK = findViewById(R.id.btnOK);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        dataProvider = new DataProvider(this);
        data = new Bundle();
        myUrl = "http://tomnab.fr/todo-api";
        data.putString("myUrl",myUrl);
        //on vérifie la connection au réseau
        connexion = verifReseau();
        data.putBoolean("connexion",connexion);
        if(connexion){
            new PostAsyncTask3().execute();
        }
        btnOK.setVisibility(View.VISIBLE);
        edtPseudo = findViewById(R.id.edt_pseudo);
        edtPassword = findViewById(R.id.edt_password);
        btnOK.setOnClickListener(this);
        toChoixListActivity = new Intent(this, ChoixListActivity.class);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnOK){
            /// Gestion avec API Rest :
            if(connexion==true) {
                mHash = null;
                pseudo = edtPseudo.getText().toString();
                password = edtPassword.getText().toString();
                //Asynctack pour trouver si la connection est autorisée
                (new PostAsyncTask()).execute();
            }
            /// Gestion avec la BDD
            else {
                pseudo = edtPseudo.getText().toString();
                password = edtPassword.getText().toString();
                Log.i("La connexion n'est pas","faite");
                (new PostAsyncTask2()).execute();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        /*
        myUrl = mSettings.getString("url","");
        Log.i("url API sauvegardé =",myUrl);
        */
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings) {
            Intent toSettings = new Intent(this,SettingsActivity.class);
            startActivity(toSettings);
        }
        return super.onOptionsItemSelected(item);
    }


    ///////////////////  gestion reseau ///////////////////

    public boolean verifReseau(){
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {

            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }

            }
        }

        Log.i("DETECTION:",sType);
        return bStatut;
    }

    //On implémente également la BDD au fur et a mesure qu'on charge des infos
    public class PostAsyncTask extends AsyncTask<Object,Void,String> {
        @Override protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progess).setVisibility(View.VISIBLE);
        }

        @Override protected String doInBackground(Object... objects) {
            mHash=dataProvider.getHash(myUrl,pseudo,password,"POST");
            //On va récupérer la valeur de l'ID de l'user en cours et on ajoute à la BDD
            JSONArray listTemp = dataProvider.getListUser(myUrl,mHash);
            int idUser=0;
            try {
                for(int i=0; i<listTemp.length();i++){
                    if (listTemp.getJSONObject(i).getString("pseudo").equals(pseudo)){
                        idUser = listTemp.getJSONObject(i).getInt("id");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();}

            //On ajoute l'user à la BDD
            data.putInt("idUser",idUser);
            dataProvider.insertUser(new ProfilListeToDo(pseudo,password, idUser));
            return  mHash;
        }

        @Override protected void onPostExecute(String hash) {
            super.onPostExecute(hash);
            mHash=hash;
            if(mHash!="" && mHash!=null && mHash!="erreur avec l'objet Json"){
                Log.i("Connection","réussie ! ");
                Log.i("mHash=",mHash);
                //On stock le hash et la liste de Lists dans les préférences
                mEditor = mSettings.edit();
                mEditor.clear();
                mEditor.putString("mHash", mHash);
                mEditor.commit();
                toChoixListActivity.putExtras(data);
                //On lance l'activité suivante
                startActivity(toChoixListActivity);
            }
            else if (mHash=="" || mHash=="erreur avec l'objet Json"){
                Toast toast = Toast.makeText(getApplicationContext(), "Mauvais Pseudo/mdp", Toast.LENGTH_SHORT);
                toast.show();
        }
            findViewById(R.id.progess).setVisibility(View.GONE);
        }

    }


    /////////////////// gestion bdd mode hors ligne ///////
    public class PostAsyncTask2 extends AsyncTask<Object,Void, List<ProfilListeToDo>> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progess).setVisibility(View.VISIBLE);
        }

        @Override protected List<ProfilListeToDo> doInBackground(Object... objects) {
            return  dataProvider.loadUsers();
        }

        @Override protected void onPostExecute(List<ProfilListeToDo> listUsers) {
            super.onPostExecute(listUsers);
            for (ProfilListeToDo user :  listUsers){
                if(user.getLogin().equals(pseudo) && user.getPasse().equals(password)){
                    data.putInt("idUser", user.getIdUser());
                    //On lance l'activité suivante
                    toChoixListActivity.putExtras(data);
                    findViewById(R.id.progess).setVisibility(View.GONE);
                    startActivity(toChoixListActivity);
                }
            }
            findViewById(R.id.progess).setVisibility(View.GONE);
        }
    }



    ////////////// Quand on se connecte, on met à jour l'API selon ce qui a été modifié sur la BDD///////
    public class PostAsyncTask3 extends AsyncTask<Object,Void, List<ItemToDo>>{
        @Override protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progess).setVisibility(View.VISIBLE);
        }

        @Override protected List<ItemToDo> doInBackground(Object... objects) {
            mHash=dataProvider.getHash(myUrl,"tom","web","POST");
            listItemModifie = dataProvider.getItemModifie();
            for(ItemToDo item : listItemModifie){
                dataProvider.requete(myUrl + "/lists/"+ item.getIdListAssocie()
                        +"/items/"+ item.getIdItem()
                        +"?check=" + item.getFait()
                        + "+&hash="+mHash,"PUT");
            }
            return  listItemModifie;
        }

        @Override protected void onPostExecute(List<ItemToDo> listItemModifie) {
            super.onPostExecute(listItemModifie);
            findViewById(R.id.progess).setVisibility(View.GONE);
        }

    }

}
