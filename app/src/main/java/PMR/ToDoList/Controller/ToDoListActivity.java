package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

import static PMR.ToDoList.Controller.MainActivity.EXTRA_LOGIN;
import static PMR.ToDoList.Controller.MainActivity.myUsersList;
import static android.content.Intent.EXTRA_USER;

/*
Classe lié à l'activité qui affiche l'ensemble des to do lists de l'utilisateur.
On peut également créer et détruire des to do lists via cette activité.
 */
public class ToDoListActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView toDoListRecyclerView;
    private ToDoListAdapter toDoListAdapter;
    private RecyclerView.LayoutManager toDoListLayoutManager;

    //PARTIE DONNEES
    public static ArrayList<ToDoList> toDoLists;

    //INSERT TODOLIST
    private Button btnInsertToDoList;
    private EditText textInsertToDoList;

    //USER DE LA TACHE
    private User user;

    public static final String EXTRA_IDLIST = "IDLIST";

    //Fonction pour la création de toasts pour le débug notamment.
    private void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    /*
    Méthode onCreate
    On récupère le login passé depuis la main activité dans l'intent. S'il correspond
    à un des login enregistrés dans la liste des utilisateurs, on récupère l'utilisateur
    concerné pour extraire ses to do listes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        buildToolbar();

        /*
        On récupère le login passé depuis la main activité dans l'intent. S'il correspond
        à un des login enregistrés dans la liste des utilisateurs, on récupère l'utilisateur
        concerné pour extraire ses to do listes.
         */
        Intent intentMain = getIntent();
        user = intentMain.getParcelableExtra(EXTRA_LOGIN);

        AsyncTask task = new PostAsyncTask();
        task.execute();

        // Création items pour l'insertion des to do lists.
        btnInsertToDoList=findViewById(R.id.btnInsertToDoList);
        textInsertToDoList=findViewById(R.id.textInsertToDoList);

        //BOUTON D'INSERTION D'UNE TO DO LIST
        btnInsertToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToDoList= textInsertToDoList.getText().toString();
                textInsertToDoList.setText(""); // on réinitialise la zone d'insertion de to do list

                if (!nameToDoList.equals("")){
                    //user.ajouteListe(new ToDoList(nameToDoList));
                    toDoListAdapter.notifyItemInserted(toDoListAdapter.getItemCount()-1);
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
                intent.putExtra(EXTRA_LOGIN, user);
                intent.putExtra(EXTRA_IDLIST, user.getToDoLists().get(position));
                alerter(String.valueOf(user.getToDoLists().get(position).getId()));
                startActivity(intent);            }
            //BOUTON QUAND ON CLIQUE SUR DELETE
            @Override
            public void onDeleteClick(int position) {

                toDoLists.remove(position);
                user.setToDoLists(toDoLists);
                toDoListAdapter.notifyItemRemoved(position);
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mes To Do Lists");
    }

    //PARTIE ASYNCTASK

    public class PostAsyncTask extends AsyncTask<Object, Void, ArrayList<ToDoList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ToDoList> doInBackground(Object... objects) {
            try {
                return (new DataProvider()).getToDoLists(user.getHash(), "POST");
            } catch (JSONException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ToDoList> myToDoList){
            super.onPostExecute(myToDoList);
            user.setToDoLists(myToDoList);
            buildRecyclerView(myToDoList);
        }
    }
}
