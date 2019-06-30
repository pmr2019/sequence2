package PMR.ToDoList.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import PMR.ToDoList.data.Model.ToDoList;
import PMR.ToDoList.data.Model.User;
import PMR.ToDoList.R;
import PMR.ToDoList.data.api.DataProvider;
import PMR.ToDoList.data.database.Database;
import PMR.ToDoList.data.database.dao.ToDoListDao;

import static PMR.ToDoList.ui.MainActivity.EXTRA_CONNEXIONOK;
import static PMR.ToDoList.ui.MainActivity.EXTRA_LOGIN;

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
    private String nameToDoList;
    private Integer idList;

    public static final String EXTRA_HASH = "TAG_HASH";
    public static final String EXTRA_TODOLIST = "TAG_TODOLIST";

    // GESTION DE LA CONNEXION
    private Boolean connexionOk;

    ToDoListDao toDoListDao;

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
        On initialise le dao
         */
        toDoListDao= Database.getDatabase(this).toDoListDao();

        /*
        On récupère le login passé depuis la main activité dans l'intent. S'il correspond
        à un des login enregistrés dans la liste des utilisateurs, on récupère l'utilisateur
        concerné pour extraire ses to do listes.
         */
        Intent intentMain = getIntent();
        user = intentMain.getParcelableExtra(EXTRA_LOGIN);
        String stringConnexionOk = intentMain.getStringExtra(EXTRA_CONNEXIONOK);
        connexionOk = (stringConnexionOk.equals("true"));

//        alerter(connexionOk.toString());

        // On récupère une référence sur les inputs
        btnInsertToDoList=findViewById(R.id.btnInsertToDoList);
        textInsertToDoList=findViewById(R.id.textInsertToDoList);

        // Si on avait de la connexion à l'écran de démarrage, on est en mode "En ligne" et
        // On fait comme avant à la seule différence qu'on enregistre les données récupérées
        // dans le SQL en plus
        if (connexionOk==true){
            btnInsertToDoList.setEnabled(true);
            AsyncTask task = new PostAsyncTask();
            task.execute();
        }

        //Si on n'avait pas de connexion à l'écran de démarrage, on est en mode "Hors Ligne" et
        // On récupère les informations du SQL pour les afficher

        else {
            textInsertToDoList.setEnabled(false);
            btnInsertToDoList.setEnabled(false);

            ArrayList<ToDoList> myToDoList=getToDoListsFromSQL(user);
            user.setToDoLists(myToDoList);
            buildRecyclerView(myToDoList);
        }



        //BOUTON D'INSERTION D'UNE TO DO LIST
        btnInsertToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameToDoList = textInsertToDoList.getText().toString();
                textInsertToDoList.setText(""); // on réinitialise la zone d'insertion de to do list

                if (!nameToDoList.equals("")){
                    AsyncTask task2 = new PostAsyncTaskAdd(); //on appelle l'asynctask responsable
                    task2.execute();                          //de la création d'une nouvelle liste

                    AsyncTask task3 = new PostAsyncTask();    //on appelle l'asynctask qui récupère
                    task3.execute();                          //la liste des ToDoLists --> petit
                    //défaut, réinitialise le layout

                    toDoListAdapter.notifyItemInserted(toDoListAdapter.getItemCount()-1);
                }
            }
        });


    }


    // FONCTIONS SQL

    private ArrayList<ToDoList> getToDoListsFromSQL(User user) {
        return (ArrayList<ToDoList>)toDoListDao.getAllUserToDoLists(user.getIdUser());
    }

    private void saveToDoListsToSQL(ArrayList<ToDoList> myToDoList) {
        toDoListDao.deleteAllUserToDoLists(myToDoList);
        toDoListDao.deleteAllToDoLists();
        for (int i = 0; i < myToDoList.size(); i++) {
            toDoListDao.insert(myToDoList.get(i));
        }
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

                // Si on a de la connexion, on passe aussi le hash pour faire les requêtes
                if(connexionOk==true){
                    intent.putExtra(EXTRA_HASH, user.getHash());
                }
                intent.putExtra(EXTRA_TODOLIST, user.getToDoLists().get(position));
                intent.putExtra(EXTRA_CONNEXIONOK,connexionOk.toString());
                startActivity(intent);
            }

        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mes To Do Lists");
    }

    //PARTIE ASYNCTASK

    //Asynctask qui permet de récupérer la liste des ToDoLists associées à l'utilisateur

    public class PostAsyncTask extends AsyncTask<Object, Void, ArrayList<ToDoList>> {


        @Override
        protected ArrayList<ToDoList> doInBackground(Object... objects) {
            try {
                return (new DataProvider()).getToDoLists(user.getHash(), "GET",user.getIdUser());
            } catch (JSONException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ToDoList> myToDoList){
            super.onPostExecute(myToDoList);
            // On enregistre les to do lists dans le SQL
            saveToDoListsToSQL(myToDoList);
            user.setToDoLists(myToDoList);
            buildRecyclerView(myToDoList);
        }
    }



    //Asynctask qui permet de créer une nouvelle liste sur l'API

    public class PostAsyncTaskAdd extends AsyncTask<Object, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Object... objects) {
            (new DataProvider()).postToDoList(nameToDoList, user.getHash(), "POST");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer id){
            super.onPostExecute(id);
            idList=id;

        }
    }
}
