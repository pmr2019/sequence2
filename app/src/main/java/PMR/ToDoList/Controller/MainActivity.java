package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    //GESTION DE LA TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    //GESTION DES INFORMATIONS DE CONNEXION
    private EditText edtPseudo;
    private TextView txtPseudo;

    private EditText edtMdp;
    private TextView txtMdp;

    private Button btnConnexion;

    //LISTE DES UTILISATEURS ENREGISTRÉS DANS LA BASE DE DONNÉES
    public static ArrayList<User> myUsersList;

    //UTILISATEUR INITIANT LA CONNEXION
    private User myUser;
    private String pseudo;
    private String password;

    //GESTION DE LA CONNEXION A INTERNET
    private NetworkStateReceiver networkStateReceiver;
    private TextView etatConnexion;

    //GESTION DES INFORMATIONS A ENREGISTRER
    public static final String EXTRA_LOGIN = "LOGIN";

    // METHODE POUR LES TOASTS
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AJOUT DES INFORMATIONS DE LA TOOLBAR
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connexion");


        // GESTION DE LA CONNEXION
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        etatConnexion= (TextView) findViewById(R.id.etatConnexion);

        //BIND DES VIEWS POUR LA CONNEXION
        edtPseudo = (EditText) findViewById(R.id.edtPseudo);
        btnConnexion = (Button) findViewById(R.id.btnPseudo);
        txtPseudo = (TextView) findViewById(R.id.txtPseudo);
        edtMdp = (EditText) findViewById(R.id.edtMdp);
        txtMdp = (TextView) findViewById(R.id.txtMdp);

        //LISTENER SUR LE BOUTON DE CONNEXION
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                User myUser = null;
                String login = edtPseudo.getText().toString();

                // Pour pouvoir appuyer sur OK, on doit avoir les deux champs mot de passe et
                //login remplis
                if ((edtPseudo.getText().toString().matches("")) |
                        (edtMdp.getText().toString().matches(""))) {
                    alerter("Entrez un pseudo et un mot de passe");

                }

                else{

                    pseudo = edtPseudo.getText().toString();
                    password = edtMdp.getText().toString();

                    AsyncTask task = new PostAsyncTask();
                    task.execute();

                }
            }
        });

    }

    // On retire le listener de connexion lorsqu'on quitte l'application
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    //Ajout du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupseudo, menu);
        return true;
    }

    //Ajout de la gestion du click sur les items du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            // MENU SETTINGS AVEC LA LISTE DES UTILISATEURS
            case R.id.menu_settings:

                if (myUsersList!=null){
                    Intent toSettings = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(toSettings);
                    break;
                }
                else alerter("Veuillez d'abord créer un pseudo");

            // MENU SETTINGS URL AVEC L'URL UTILISEE PAR L'API
            case R.id.menu_settings_url:

                Intent toSettingsURL = new Intent(MainActivity.this,SettingsURL.class);
                startActivity(toSettingsURL);
                    break;
                }
        return super.onOptionsItemSelected(item);
    }

    //Partie GSON
    //Ecrire des données dans la mémoire interne du téléphone

    public void sauvegarderUserToJsonFile(ArrayList myList) {

        final GsonBuilder builder = new GsonBuilder(); //assure la qualité des données Json
        final Gson gson = builder.setPrettyPrinting().create();
        String fileName = "pseudos"; //nom du fichier Json
        FileOutputStream outputStream; //permet de sérialiser correctement user

        String fileContents = gson.toJson(myList);

        try {
            outputStream = openFileOutput("pseudos", Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Log.i("TODO_Romain", "Sauvegarde du fichier Json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction recréant à chaque ouverture de l'appli une liste de users
    public ArrayList<User> getUsersFromFile() {
        Gson gson = new Gson();
        String json = "";
        ArrayList<User> usersList = null;
        try {
            FileInputStream inputStream = openFileInput("pseudos");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                                new BufferedInputStream(inputStream), StandardCharsets.UTF_8));
            usersList = gson.fromJson(br, new TypeToken<List<User>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // SI ON A PAS REUSSI A RECUPERER DES ELEMENTS DANS LE FICHIER JSON, ON RETOURNE
        // UNE ARRAYLIST VIDE
        if (usersList==null) return new ArrayList<>();
        // SINON ON RETOURNE LA LISTE DES UTILISATEURS RECUPEREE
        else return usersList;
    }



    // PARTIE VERIFICATION DE LA CONNEXION

    //Si la connexion est ok: bouton connexion disponible et texte informatif
    @Override
    public void networkAvailable() {
        btnConnexion.setEnabled(true);
        etatConnexion.setText("Connexion OK");
    }

    //Si la connexion pas ok: bouton connexion pas disponible et texte informatif
    @Override
    public void networkUnavailable() {
        btnConnexion.setEnabled(false);
        etatConnexion.setText("Veuillez vous connecter à internet");
    }

    //PARTIE ASYNCTASK

    public class PostAsyncTask extends AsyncTask<Object, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... objects) {
            try {
                return (new DataProvider()).getHash(pseudo, password, "POST");
            } catch (JSONException e) {
                e.printStackTrace();
                return "Veuillez rentrer un pseudo et un mot de passe valides";
            }
        }

        @Override
        protected void onPostExecute(String hash){
            super.onPostExecute(hash);
            alerter(hash);

            if (hash!=null) {
                myUser = new User (pseudo, password);
                myUser.setHash(hash);

                Intent toToDoListActivity = new Intent(MainActivity.this, ToDoListActivity.class);
                toToDoListActivity.putExtra(EXTRA_LOGIN, myUser.getHash());
                startActivity(toToDoListActivity);
            }

        }

    }
}






