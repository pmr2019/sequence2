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


        Intent intentToDoList = getIntent();
        for(ToDoList tdl : toDoLists){
            if(tdl.getIdList().equals(intentToDoList.getSerializableExtra(EXTRA_IDLIST))) todolist = tdl;
        }

        tasks=todolist.getLesItems();

        //tasks.add(new Task("toDo2"));

        buildRecyclerView(tasks);

        btnInsertTask=findViewById(R.id.btnInsertTask);
        textInsertTask=findViewById(R.id.textInsertTask);

        //BOUTON D'INSERTION D'UNE TO DO LIST
        btnInsertTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTask= textInsertTask.getText().toString();
                textInsertTask.setText("");

                if (!nameTask.equals("")){
                    tasks.add(new Task(nameTask));
                    sauvegarderToJsonFile(toDoLists);
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
                Toast toast=Toast.makeText(getApplicationContext(),tasks.get(position).getFait().toString(),Toast.LENGTH_SHORT);
                toast.show();
            }

            //BOUTON QUAND ON CLIQUE SUR DELETE
            @Override
            public void onDeleteClick(int position) {
                tasks.remove(position);
                sauvegarderToJsonFile(myUsersList);
                taskAdapter.notifyItemRemoved(position);
            }

            //BOUTON QUAND ON CLIQUE SUR UNE CHECKBOX
            @Override
            public void onCheckBoxClick(int position) {
                if (tasks.get(position).getFait()) {
                    tasks.get(position).setFait(false);
                }
                else {tasks.get(position).setFait(true);}
                sauvegarderToJsonFile(toDoLists);

            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void sauvegarderToJsonFile(ArrayList myList) {

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
