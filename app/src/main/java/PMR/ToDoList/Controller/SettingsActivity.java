package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;

import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

public class SettingsActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView settingRecyclerView;
    private SettingsAdapter settingsAdapter;
    private RecyclerView.LayoutManager settingLayoutManager;

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
}
