package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import PMR.ToDoList.Model.Task;
import PMR.ToDoList.R;

public class TasksActivity extends AppCompatActivity {

    // PARTIE TOOLBAR
    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW
    private RecyclerView taskRecyclerView;
    private TasksAdapter taskAdapter;
    private RecyclerView.LayoutManager taskLayoutManager;

    //PARTIE DONNEES
    private ArrayList<Task> tasks;

    //INSERT TODOLIST
    private Button btnInsertTask;
    private EditText textInsertTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        buildToolbar();

        tasks =new ArrayList<>();

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
                tasks.get(position).setDescription("clicked");
                taskAdapter.notifyItemChanged(position);            }
            //BOUTON QUAND ON CLIQUE SUR DELETE
            @Override
            public void onDeleteClick(int position) {
                tasks.remove(position);
                taskAdapter.notifyItemRemoved(position);
            }
        });
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
