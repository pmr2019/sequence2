package PMR.ToDoList.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import PMR.ToDoList.data.Model.Task;
import PMR.ToDoList.data.Model.ToDoList;
import PMR.ToDoList.R;
import PMR.ToDoList.data.api.DataProvider;
import PMR.ToDoList.data.database.Database;
import PMR.ToDoList.data.database.dao.TaskDao;

import static PMR.ToDoList.ui.MainActivity.EXTRA_CONNEXIONOK;
import static PMR.ToDoList.ui.ToDoListActivity.EXTRA_HASH;
import static PMR.ToDoList.ui.ToDoListActivity.EXTRA_TODOLIST;

public class TasksActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView taskRecyclerView;
    private TasksAdapter taskAdapter;
    private RecyclerView.LayoutManager taskLayoutManager;

    //PARTIE DONNEES
    private String nameTask;
    private Task taskChecked;

    //INSERT TASK
    private Button btnInsertTask;
    private EditText textInsertTask;

    //TO DO LIST DE LA TACHE
    private ToDoList todolist;
    private String hash;

    //PARTIE GESTION DE LA CONNEXION
    private Boolean connexionOk;

    //BASE DE DONNEES
    private TaskDao taskDao;

    private void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        buildToolbar();

        /*On récupère la to do list passée depuis l'activité to do list dans l'intent. S'il correspond
        à une des to do lists enregistrés dans la liste des utilisateurs, on récupère l'utilisateur
        concerné pour extraire ses to do listes.
         */

        Intent intentTaskActivity = getIntent();
        todolist = intentTaskActivity.getParcelableExtra(EXTRA_TODOLIST);
        String stringConnexionOk = intentTaskActivity.getStringExtra(EXTRA_CONNEXIONOK);
        connexionOk = (stringConnexionOk.equals("true"));

        if (connexionOk) hash = intentTaskActivity.getStringExtra(EXTRA_HASH);

/*
        On initialise le dao
         */
        taskDao= Database.getDatabase(this).taskDao();


        // On récupère une référence sur les inputs
        btnInsertTask=findViewById(R.id.btnInsertTask);
        textInsertTask=findViewById(R.id.textInsertTask);

        // Si on avait de la connexion à l'écran de démarrage, on est en mode "En ligne" et
        // On fait comme avant à la seule différence qu'on enregistre les données récupérées
        // dans le SQL en plus
        if (connexionOk==true){
            btnInsertTask.setEnabled(true);
            AsyncTask task = new PostAsyncTask();
            task.execute();
        }

        //Si on n'avait pas de connexion à l'écran de démarrage, on est en mode "Hors Ligne" et
        // On récupère les informations du SQL pour les afficher

        else {
            textInsertTask.setEnabled(false);
            btnInsertTask.setEnabled(false);

            ArrayList<Task> myTasks = getTasksFromSQL(todolist);

            todolist.setTasksList(myTasks);
            buildRecyclerView(myTasks);
        }


        //BOUTON D'INSERTION D'UNE TACHE
        btnInsertTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTask= textInsertTask.getText().toString();
                textInsertTask.setText("");

                if (!nameTask.equals("")){
                    //On crée une Asynctask permettant l'ajout de la nouvelle liste sur l'API, puis
                    //on fait de nouveau appel à l'AsyncTask de récupération des items pour recréer
                    //un nouveau layout

                    AsyncTask task2 = new PostAsyncTaskAdd();
                    task2.execute();

                    AsyncTask task3 = new PostAsyncTask();
                    task3.execute();

                    taskAdapter.notifyItemInserted(taskAdapter.getItemCount()-1);
                }
            }
        });


    }

    private ArrayList<Task> getTasksFromSQL(ToDoList todolist) {
        return (ArrayList<Task>)taskDao.getAllToDoListTasks(todolist.getIdToDoList());

    }

    private void saveTasksToSQL(ArrayList<Task> myTasks) {
        taskDao.deleteAllToDOListTasks(myTasks);
        for (int i = 0; i < myTasks.size(); i++) {
            taskDao.insert(myTasks.get(i));
        }
    }

    public void buildRecyclerView(ArrayList<Task> list){
        taskRecyclerView = findViewById(R.id.rvTask);
        taskRecyclerView.setHasFixedSize(true);
        taskLayoutManager = new LinearLayoutManager(this);
        taskAdapter = new TasksAdapter(list);
        taskRecyclerView.setLayoutManager(taskLayoutManager);
        taskRecyclerView.setAdapter(taskAdapter);


        taskAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {


            //BOUTON QUAND ON CLIQUE SUR UNE CHECKBOX
            @Override
            public void onCheckBoxClick(int position) {

                // On change l'état de la checkbox

                if (todolist.getTasksList().get(position).getChecked()==1) {
                    todolist.getTasksList().get(position).setChecked(0);
                }
                else {
                    todolist.getTasksList().get(position).setChecked(1);
                }

                // On récupère la tâche sur laquelle on a cliqué
                taskChecked = todolist.getTasksList().get(position);

                // On traite des cas différents suivant la disponibilité de la connexion

                taskDao.updateCheckboxTask(taskChecked);

                if (connexionOk){
                    AsyncTask task4 = new PostAsyncTaskChecked();
                    task4.execute();
                }
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mes Tasks");

    }

    //PARTIE ASYNCTASK

    //AsyncTask responsable de la récupération de la liste des items

    public class PostAsyncTask extends AsyncTask<Object, Void, ArrayList<Task>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Task> doInBackground(Object... objects) {
            try {
                return (new DataProvider()).getTasks(hash, todolist.getIdToDoList(), "GET");
            } catch (JSONException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Task> myTasks){
            super.onPostExecute(myTasks);
            ArrayList<Task> tasksFromSQL = getTasksFromSQL(todolist);

            // Si jamais les checkbox du SQL et de l'API sont différentes, on
            // met à jour celles de l'API

            for (int i = 0; i < myTasks.size(); i++) {
                for (int j = 0; j < tasksFromSQL.size(); j++) {
                    if(myTasks.get(i).getIdTask()==tasksFromSQL.get(j).getIdTask()){
                        if(myTasks.get(i).getChecked()!=tasksFromSQL.get(j).getChecked()){
                            myTasks.get(i).setChecked(tasksFromSQL.get(j).getChecked());
                        }
                    }
                }
            }
            saveTasksToSQL(myTasks);
            todolist.setTasksList(myTasks);
            buildRecyclerView(myTasks);

        }
    }



    //AsyncTask responsable de l'ajout d'un item

    public class PostAsyncTaskAdd extends AsyncTask<Object, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Object... objects) {
            (new DataProvider()).postItem(todolist.getIdToDoList(), nameTask, hash, "POST");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer id) {
            super.onPostExecute(id);
            Log.i("TAG", "Tâche bien ajoutée");
        }

    }

    //AsyncTask responsable de statut "coché" / "décoché" d'un item

    public class PostAsyncTaskChecked extends AsyncTask<Object, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Object... objects) {
            (new DataProvider()).itemChecked(todolist.getIdToDoList(), taskChecked.getIdTask(),taskChecked.getChecked(), hash, "PUT");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer id) {
            super.onPostExecute(id);
        }

    }

}
