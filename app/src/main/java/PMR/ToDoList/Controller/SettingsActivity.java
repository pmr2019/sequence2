package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

public class SettingsActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView settingRecyclerView;
    private SettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingLayoutManager;
    private User userSettings;

    //PARTIE DONNEES
    private ArrayList<User> settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buildToolbar();

        settings =new ArrayList<>();

        settings.add(new User("John"));
        settings.add(new User("Fred"));

        readUserFromJsonFile();
        settings.add(userSettings);

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
                settingsAdapter.notifyItemRemoved(position);
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Partie GSON

    public void readUserFromJsonFile() {
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
    }


}