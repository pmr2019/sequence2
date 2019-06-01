package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

import static android.content.Intent.EXTRA_USER;

public class ToDoListActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView toDoListRecyclerView;
    private ToDoListAdapter toDoListAdapter;
    private RecyclerView.LayoutManager toDoListLayoutManager;

    //PARTIE DONNEES
    private ArrayList<ToDoList> toDoLists;

    //INSERT TODOLIST
    private Button btnInsertToDoList;
    private EditText textInsertToDoList;

    //USER DE LA TACHE
    private User user;
    private ArrayList<User> usersList;

    private void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        buildToolbar();

        toDoLists =new ArrayList<>();

        toDoLists.add(new ToDoList("toDo1"));
        toDoLists.add(new ToDoList("toDo2"));

        Intent intentMain = getIntent();
        user = intentMain.getParcelableExtra(EXTRA_USER);
        alerter(user.getLogin());

        //sauvegarderToJsonFile(usersList);


        buildRecyclerView(toDoLists);

        btnInsertToDoList=findViewById(R.id.btnInsertToDoList);
        textInsertToDoList=findViewById(R.id.textInsertToDoList);

        //BOUTON D'INSERTION D'UNE TO DO LIST
        btnInsertToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToDoList= textInsertToDoList.getText().toString();
                textInsertToDoList.setText("");

                if (!nameToDoList.equals("")){
                    toDoLists.add(new ToDoList(nameToDoList));
                    toDoListAdapter.notifyItemInserted(toDoListAdapter.getItemCount()-1);

/*                    user.getMesListeToDo().add(new ToDoList(nameToDoList));
                    replaceUser(getUsersFromFile(), user);
                    getUsersFromFile().add(user);
                    sauvegarderToJsonFile(getUsersFromFile());
                    alerter("sauvegarde");*/
                }

            }
        });


    }

    public void buildRecyclerView(ArrayList<ToDoList> list){
        toDoListRecyclerView = findViewById(R.id.rvTodoList);
        toDoListRecyclerView.setHasFixedSize(true);
        toDoListLayoutManager = new LinearLayoutManager(this);
        toDoListAdapter = new ToDoListAdapter(list);
        toDoListRecyclerView.setLayoutManager(toDoListLayoutManager);
        toDoListRecyclerView.setAdapter(toDoListAdapter);


        toDoListAdapter.setOnItemClickListener(new ToDoListAdapter.OnItemClickListener() {
            @Override
            //BOUTON QUAND ON CLIQUE SUR UNE CARD
            public void onItemClick(int position) {
                Intent intent=new Intent(ToDoListActivity.this,TasksActivity.class);
                startActivity(intent);            }
            //BOUTON QUAND ON CLIQUE SUR DELETE
            @Override
            public void onDeleteClick(int position) {
                toDoLists.remove(position);
                toDoListAdapter.notifyItemRemoved(position);
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

/*    public void sauvegarderToJsonFile(ArrayList myList) {

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
        if (usersList != null) {
            Log.i(TAG, "getUsersFromFile: ");
            for (User p : usersList) {
                p.onDeserialization();
                autoCompleteAdapter.add(p.getUsername());
            }
        }

        return usersList;
    }*/

    public void replaceUser (ArrayList<User> myList, User myUser){

        for (User u : myList){
            if (u.getLogin()==myUser.getLogin()){
                myList.remove(u);
                myList.add(myUser);
            }
        }
    }
}
