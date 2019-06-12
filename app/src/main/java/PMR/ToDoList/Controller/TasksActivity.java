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
import static PMR.ToDoList.Controller.ToDoListActivity.EXTRA_IDLIST;
import static PMR.ToDoList.Controller.ToDoListActivity.toDoLists;

public class TasksActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView taskRecyclerView;
    private TasksAdapter taskAdapter;
    private RecyclerView.LayoutManager taskLayoutManager;

    //PARTIE DONNEES
    private ArrayList<Task> tasks;

    //INSERT TASK
    private Button btnInsertTask;
    private EditText textInsertTask;

    //TO DO LIST DE LA TACHE
    private ToDoList todolist;

    //USER EN QUESTION
    private User user;

    private void alerter(String s) {
        Toast myToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        myToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        buildToolbar();

        //tasks.add(new Task("toDo1"));

/*
        On récupère la to do list passée depuis l'activité to do list dans l'intent. S'il correspond
        à une des to do lists enregistrés dans la liste des utilisateurs, on récupère l'utilisateur
        concerné pour extraire ses to do listes.
         */

        Intent intentMain = getIntent();
        user = intentMain.getParcelableExtra(EXTRA_LOGIN);
        todolist = intentMain.getParcelableExtra(EXTRA_IDLIST);

        alerter(String.valueOf(todolist.getId()));
        alerter(todolist.getLabel());

        //AsyncTask task = new PostAsyncTask();
        //task.execute();

        btnInsertTask=findViewById(R.id.btnInsertTask);
        textInsertTask=findViewById(R.id.textInsertTask);

        //BOUTON D'INSERTION D'UNE TO DO LIST
        btnInsertTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTask= textInsertTask.getText().toString();
                textInsertTask.setText("");

                if (!nameTask.equals("")){

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
                //Toast toast=Toast.makeText(getApplicationContext(),tasks.get(position).getFait().toString(),Toast.LENGTH_SHORT);
                //toast.show();
            }

            //BOUTON QUAND ON CLIQUE SUR DELETE
            @Override
            public void onDeleteClick(int position) {
                tasks.remove(position);
                taskAdapter.notifyItemRemoved(position);
            }

            //BOUTON QUAND ON CLIQUE SUR UNE CHECKBOX
            @Override
            public void onCheckBoxClick(int position) {
                if (tasks.get(position).getChecked()==1) {
                    tasks.get(position).setChecked(0);
                }
                else {tasks.get(position).setChecked(1);}
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mes Tasks");

    }

    public class PostAsyncTask extends AsyncTask<Object, Void, ArrayList<Task>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Task> doInBackground(Object... objects) {
            try {
                return (new DataProvider()).getTasks(user.getHash(), todolist.getId(), "GET");
            } catch (JSONException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Task> myTasks){
            super.onPostExecute(myTasks);
            alerter(String.valueOf(todolist.getId()));
            buildRecyclerView(myTasks);
        }
    }

}
