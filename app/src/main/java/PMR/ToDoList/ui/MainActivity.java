package PMR.ToDoList.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.NetworkStateReceiver;
import PMR.ToDoList.data.Model.User;
import PMR.ToDoList.R;
import PMR.ToDoList.data.api.DataProvider;
import PMR.ToDoList.data.database.Database;
import PMR.ToDoList.data.database.dao.UserDao;

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
    private TextView tvUrlApi;
    public static String urlApi;

    //UTILISATEUR INITIANT LA CONNEXION
    private User myUser;
    private String pseudo;
    private String password;

    //GESTION DE LA CONNEXION A INTERNET
    private NetworkStateReceiver networkStateReceiver;
    private TextView etatConnexion;
    private Boolean connexionOk;
    private TextView erreurConnexion;

    //GESTION DES INFORMATIONS A ENREGISTRER
    public static final String EXTRA_LOGIN = "LOGIN";
    public static final String EXTRA_CONNEXIONOK = "CONNEXIONOK";

    // BASE DE DONNEES
    UserDao userDao;

    // METHODE POUR LES TOASTS
    public void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    /*
    ON CREATE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userDao= Database.getDatabase(this).userDao();
        myUsersList = (ArrayList<User>)userDao.getAllUsers();


        try {
            urlApi= getUrlApiFromJson();
        } catch (IOException e) {
            e.printStackTrace();
            urlApi= "http://tomnab.fr/todo-api/";
            sauvegarderUrlApiToJsonFile(urlApi);
        }

        //AJOUT DES INFORMATIONS DE LA TOOLBAR
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connexion");


        // GESTION DE LA CONNEXION
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        etatConnexion= findViewById(R.id.etatConnexion);
        erreurConnexion = findViewById(R.id.erreurConnexion);
        erreurConnexion.setText("");

        //BIND DES VIEWS POUR LA CONNEXION
        edtPseudo = findViewById(R.id.edtPseudo);
        btnConnexion = findViewById(R.id.btnPseudo);
        txtPseudo = findViewById(R.id.txtPseudo);
        edtMdp = findViewById(R.id.edtMdp);
        txtMdp = findViewById(R.id.txtMdp);

        //LISTENER SUR LE BOUTON DE CONNEXION
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pour pouvoir appuyer sur OK, on doit avoir les deux champs mot de passe et
                //login remplis
                if ((edtPseudo.getText().toString().matches("")) |
                        (edtMdp.getText().toString().matches(""))) {
                    alerter("Entrez un pseudo et un mot de passe");

                }
                else{

                    // ON ENREGISTRE LES PSEUDOS ET PASSWORD UTILISES
                    pseudo = edtPseudo.getText().toString();
                    password = edtMdp.getText().toString();

                    if (connexionOk){
                        AsyncTask task = new PostAsyncTask();
                        task.execute();
                    }

                    else {
                        // Si on a pas de connexion, on regarde si l'utilisateur
                        //correspondant à ce pseudo/password est enregistré
                        String hashTemporaire="";
                        int idTemporaire=-1;
                        myUser=new User(idTemporaire,pseudo,password,hashTemporaire);

                        Boolean estDansSettings=false;
                        Boolean pseudoOk=false;
                        Boolean passwordOk=false;

                        for (int i = 0; i < myUsersList.size(); i++) {
                            if (myUsersList.get(i).getPseudo().equals(pseudo)){
                                pseudoOk=true;
                                if (myUsersList.get(i).getPassword().equals(password)){
                                    passwordOk=true;
                                    myUser.setIdUser(myUsersList.get(i).getIdUser());
                                    myUser.setHash(myUsersList.get(i).getHash());
                                }
                            }
                        }

                        // Si le mot de passe est incorrect, on informe l'utilisateur
                        if ((pseudoOk==true) && (passwordOk==false)){
                            erreurConnexion.setText("Mot de passe incorrect");
                        }

                        // Si l'utilisateur n'a jamais été enregistré, on informe l'utilisateur
                        if (pseudoOk==false){
                            erreurConnexion.setText("Sans connexion, impossible d'accéder aux ToDoLists d'un utilisateur jamais renseigné");
                        }


                        // Si l'utilisateur est enregistré, on passe en mode Hors connexion
                        if ((pseudoOk==true) && (passwordOk==true)){
                            Intent toToDoListActivity = new Intent(MainActivity.this, ToDoListActivity.class);
                            toToDoListActivity.putExtra(EXTRA_LOGIN, myUser);
                            toToDoListActivity.putExtra(EXTRA_CONNEXIONOK,connexionOk.toString());
                            startActivity(toToDoListActivity);
                        }

                    }
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

                if (!myUsersList.isEmpty()){
                    Intent toSettings = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(toSettings);
                    break;
                }
                else {
                    alerter("Aucun user dans la base de donnée");
                    break;
                }

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

    public void sauvegarderUrlApiToJsonFile(String urlApi) {

        final GsonBuilder builder = new GsonBuilder(); //assure la qualité des données Json
        final Gson gson = builder.setPrettyPrinting().create();
        String fileName = "UrlApi"; //nom du fichier Json
        FileOutputStream outputStream; //permet de sérialiser correctement user

        String fileContents = gson.toJson(urlApi);

        try {
            outputStream = openFileOutput("UrlApi", Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction recréant à chaque ouverture de l'appli une liste de users
    private String getUrlApiFromJson() throws IOException {
        InputStream in = openFileInput("UrlApi");
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // PARTIE VERIFICATION DE LA CONNEXION

    //Si la connexion est ok: bouton connexion disponible et texte informatif
    @Override
    public void networkAvailable() {
        connexionOk=true;
        erreurConnexion.setText("");
        etatConnexion.setText("Connexion OK");
    }

    //Si la connexion pas ok: bouton connexion pas disponible et texte informatif
    @Override
    public void networkUnavailable() {
        connexionOk=false;
        etatConnexion.setText(
                "Attention ! Vous n'êtes pas connectés à internet.");
    }

    //PARTIE ASYNCTASK

    //Asynctask qui permet d'établir la connexion à l'API, et récupérer le hash de l'utilisateur

    public class PostAsyncTask extends AsyncTask<Object, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Object... objects) {
            ArrayList<String> donneesUser=new ArrayList<>();
            DataProvider dataProvider=new DataProvider();
            try {donneesUser.add(dataProvider.getHash(pseudo, password, "POST"));
                 donneesUser.add(dataProvider.getId(pseudo,donneesUser.get(0),"GET"));
                 return (donneesUser);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> donneesUser){
            super.onPostExecute(donneesUser);

            if (donneesUser==null){
                alerter("Veuillez entrer un pseudo et un mot de passe valides");
            }

            else {
                myUser = new User (Integer.parseInt(donneesUser.get(1)),pseudo, password,donneesUser.get(0));

                alerter(myUser.toString());

                boolean estDansSettings=false;


                // ON REGARDE SI L'UTILISATEUR EST DEJA ENREGISTRE
                for (int i = 0; i < myUsersList.size(); i++) {
                    if (myUsersList.get(i).equals(myUser)) {
                        estDansSettings=true;
                    }
                }


                if (!estDansSettings){
                    myUsersList.add(myUser);
                    userDao.insert(myUser);
                }

                Intent toToDoListActivity = new Intent(MainActivity.this, ToDoListActivity.class);
                toToDoListActivity.putExtra(EXTRA_LOGIN, myUser);
                toToDoListActivity.putExtra(EXTRA_CONNEXIONOK,connexionOk.toString());
                startActivity(toToDoListActivity);
            }
        }
    }
}






