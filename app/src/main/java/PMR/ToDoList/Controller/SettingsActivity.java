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

import static android.content.Intent.EXTRA_USER;

public class SettingsActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView settingRecyclerView;
    private SettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingLayoutManager;

    //PARTIE DONNEES
    private ArrayList<User> settings = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buildToolbar();

        for (User u : getUsersFromFile())
            settings.add(u);


        buildRecyclerView(settings);
    }

    public void buildRecyclerView(ArrayList<User> list){
        settingRecyclerView= findViewById(R.id.rvTodoList);
        settingRecyclerView.setHasFixedSize(true);
        settingLayoutManager = new LinearLayoutManager(this);
        settingsAdapter = new SettingsAdapter(list);
        settingRecyclerView.setLayoutManager(settingLayoutManager);
        settingRecyclerView.setAdapter(settingsAdapter);


        settingsAdapter.setOnItemClickListener(new SettingsAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                settings.remove(position);
                sauvegarderUserToJsonFile(settings);
                settingsAdapter.notifyItemRemoved(position);
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Partie GSON

/*    public void readUserFromJsonFile() {
        String sJasonLu = "";
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();

        try {
            FileInputStream inputStream = openFileInput("pseudos");
            int content;
            while ((content = inputStream.read()) != -1) {
                sJasonLu = sJasonLu + (char)content;
            }
            inputStream.close();
            userSettings = (User) gson.fromJson(sJasonLu, User.class);
        } catch (Exception e) {
        }
    }*/

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

}