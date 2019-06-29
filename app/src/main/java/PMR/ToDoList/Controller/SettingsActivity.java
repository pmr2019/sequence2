package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

import static PMR.ToDoList.Controller.MainActivity.myUsersList;
import static android.content.Intent.EXTRA_USER;

/*
Activité comportant un recyclerview qui affiche tous les utilisateurs déjà rentrés dans
l'application.
 */
public class SettingsActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView settingRecyclerView;
    private SettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingLayoutManager;

    //PARTIE DONNEES
    private ArrayList<User> settings = myUsersList;

/*
Initialisation de la toolbar et du recyclerview lié à cette activité
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buildToolbar();

        buildRecyclerView(settings);
    }

    /*
    Création du Recyclerview et de l'adapter
     */
    public void buildRecyclerView(ArrayList<User> list){
        settingRecyclerView= findViewById(R.id.rvTodoList);
        settingRecyclerView.setHasFixedSize(true);
        settingLayoutManager = new LinearLayoutManager(this);
        settingsAdapter = new SettingsAdapter(list);
        settingRecyclerView.setLayoutManager(settingLayoutManager);
        settingRecyclerView.setAdapter(settingsAdapter);

        //Réalisation de la fonction "delete" dans l'activité settings.
        settingsAdapter.setOnItemClickListener(new SettingsAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                settings.remove(position); //On retire de la liste des utilisateurs celui qu'on a cliqué
                sauvegarderUserToJsonFile(settings); //On resauvegarde le fichier json
                settingsAdapter.notifyItemRemoved(position); //On met à jour
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Partie GSON

    //Fonction de sauvegarde
    public void sauvegarderUserToJsonFile(ArrayList myList) {

        final GsonBuilder builder = new GsonBuilder(); //assure la qualité des données Json
        final Gson gson = builder.setPrettyPrinting().create();
        String fileName = "pseudos&Hashs"; //nom du fichier Json
        FileOutputStream outputStream; //permet de sérialiser correctement user

        String fileContents = gson.toJson(myList);

        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Log.i("TODO_Romain", "Sauvegarde du fichier Json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}