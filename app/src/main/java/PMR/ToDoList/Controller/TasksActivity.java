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

import org.json.JSONException;

import java.io.FileOutputStream;
import java.util.ArrayList;

import PMR.ToDoList.Model.Task;
import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

import static PMR.ToDoList.Controller.MainActivity.EXTRA_LOGIN;
import static PMR.ToDoList.Controller.MainActivity.myUsersList;
import static PMR.ToDoList.Controller.ToDoListActivity.EXTRA_HASH;
import static PMR.ToDoList.Controller.ToDoListActivity.EXTRA_TODOLIST;
import static PMR.ToDoList.Controller.ToDoListActivity.toDoLists;

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
        hash = intentTaskActivity.getStringExtra(EXTRA_HASH);

        AsyncTask task = new PostAsyncTask();
        task.execute();

        btnInsertTask=findViewById(R.id.btnInsertTask);
        textInsertTask=findViewById(R.id.textInsertTask);

        //BOUTON D'INSERTION D'UNE TO DO LIST
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

    public void buildRecyclerView(ArrayList<Task> list){
        taskRecyclerView = findViewById(R.id.rvTask);
        taskRecyclerView.setHasFixedSize(true);
        taskLayoutManager = new LinearLayoutManager(this);
        taskAdapter = new TasksAdapter(list);
        taskRecyclerView.setLayoutManager(taskLayoutManager);
        taskRecyclerView.setAdapter(taskAdapter);


        taskAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
            @Override

            //BOUTON QUAND ON CLIQUE SUR UNE CARD
            public void onItemClick(int position) {
                //On récupère la TODOLIST en question
            }

            //BOUTON QUAND ON CLIQUE SUR UNE CHECKBOX
            @Override
            public void onCheckBoxClick(int position) {
                if (todolist.getTasksList().get(position).getChecked()==1) {
                    todolist.getTasksList().get(position).setChecked(0);
                    taskChecked = todolist.getTasksList().get(position);

                    //On crée une AsyncTask
                    AsyncTask task4 = new PostAsyncTaskChecked();
                    task4.execute();

                }
                else {todolist.getTasksList().get(position).setChecked(1);}
                    taskChecked = todolist.getTasksList().get(position);

                    AsyncTask task4 = new PostAsyncTaskChecked();
                    task4.execute();
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
                return (new DataProvider()).getTasks(hash, todolist.getId(), "GET");
            } catch (JSONException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Task> myTasks){
            super.onPostExecute(myTasks);
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
            (new DataProvider()).postItem(todolist.getId(), nameTask, hash, "POST");
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
            (new DataProvider()).itemChecked(todolist.getId(), taskChecked.getId(),taskChecked.getChecked(), hash, "PUT");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer id) {
            super.onPostExecute(id);
            Log.i("TAG", "Item correctement coché");
        }

    }

}
