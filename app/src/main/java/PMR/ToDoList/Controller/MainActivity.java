package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Romain";
    private EditText edtPseudo;
    private Button btnPseudo;
    private TextView txtPseudo;
    public static final String EXTRA_LOGIN = "LOGIN";
    private androidx.appcompat.widget.Toolbar toolbar;


    public static ArrayList<User> myUsersList;

    private void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPseudo = (EditText) findViewById(R.id.edtPseudo);
        btnPseudo = (Button) findViewById(R.id.btnPseudo);
        txtPseudo = (TextView) findViewById(R.id.txtPseudo);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myUsersList = getUsersFromFile();

        btnPseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                User myUser = null;
                String login = edtPseudo.getText().toString();
                Boolean autorisation = true;
                if (edtPseudo.getText().toString().matches("")) {
                    alerter("Entrez votre pseudo");
                    autorisation = false;

                }
                else if (myUsersList!=null) {
                    for (User u : myUsersList) {
                        if (u.getLogin().equals(login)) {
                            myUser = u;
                            alerter("Ce pseudo existe déjà");
                        }
                    }
                }

                else if (myUsersList==null){
                    myUsersList = new ArrayList<>();
                }

                if (myUser==null){              //si le pseudo n'existait pas
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

                Intent toSettings = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(toSettings);
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

}






