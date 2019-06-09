package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    private static final String TAG = "Romain";
    private EditText edtPseudo;
    private Button btnConnexion;
    private TextView txtPseudo;
    private EditText edtMdp;
    private TextView txtMdp;
    public static final String EXTRA_LOGIN = "LOGIN";
    private androidx.appcompat.widget.Toolbar toolbar;

    public static ArrayList<User> myUsersList;

    private NetworkStateReceiver networkStateReceiver;
    private TextView etatConnexion;

    public void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        etatConnexion= (TextView) findViewById(R.id.etatConnexion);

        //sauvegarderUserToJsonFile(myUsersList);

        edtPseudo = (EditText) findViewById(R.id.edtPseudo);
        btnConnexion = (Button) findViewById(R.id.btnPseudo);
        txtPseudo = (TextView) findViewById(R.id.txtPseudo);
        edtMdp = (EditText) findViewById(R.id.edtMdp);
        txtMdp = (TextView) findViewById(R.id.txtMdp);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myUsersList = getUsersFromFile();

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                User myUser = null;
                String login = edtPseudo.getText().toString();
                Boolean autorisation = true;
                if ((edtPseudo.getText().toString().matches(""))|
                    (edtMdp.getText().toString().matches("")))
                {
                    alerter("Entrez un pseudo et un mot de passe");
                    autorisation = false;

                }

                else if (myUsersList!=null) {
                    for (User u : myUsersList) {
                        if (u.getLogin().equals(login)) {
                            myUser = u;
                            //alerter("Ce pseudo existe déjà");
                        }
                    }
                }

                else if (myUsersList==null){
                    myUsersList = new ArrayList<>();
                    myUser = new User(login);
                    myUsersList.add(myUser);
                }

                //alerter(String.valueOf(myUsersList.size()));

                if (myUser==null){
                    myUser = new User(login);
                    myUsersList.add(myUser);
                }


                if (autorisation) {
                    intent.putExtra(EXTRA_LOGIN, myUser.getLogin());
                    sauvegarderUserToJsonFile(myUsersList);
                    startActivity(intent);
                }
            }
        });

    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupseudo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings:

                if (myUsersList!=null){
                    Intent toSettings = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(toSettings);
                    break;
                }
                else alerter("Veuillez d'abord créer un pseudo");
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
        //partie suggestions editText
/*        if (usersList != null) {
            Log.i(TAG, "getUsersFromFile: ");
            for (User p : usersList) {
                p.onDeserialization();
                autoCompleteAdapter.add(p.getUsername());
            }
        }*/

        return usersList;
    }

    @Override
    public void networkAvailable() {
        btnConnexion.setEnabled(true);
        etatConnexion.setText("Connexion OK");
    }

    @Override
    public void networkUnavailable() {
        btnConnexion.setEnabled(false);
        etatConnexion.setText("Veuillez vous connecter à internet");
    }
}






