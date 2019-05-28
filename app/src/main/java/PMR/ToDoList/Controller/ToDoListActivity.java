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

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.R;

public class ToDoListActivity extends AppCompatActivity {

    // PARTIE TOOLBAR

    private androidx.appcompat.widget.Toolbar toolbar;

    // PARTIE RECYCLERVIEW

    private RecyclerView toDoListRecyclerView;
    private RecyclerView.Adapter toDoListAdapter;
    private RecyclerView.LayoutManager toDoListLayoutManager;

    //PARTIE DONNEES

    private ArrayList<ToDoList> toDoLists;

    //INSERT TODOLIST

    private Button btnInsertToDoList;
    private EditText textInsertToDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        buildToolbar();

        toDoLists =new ArrayList<>();

        buildRecyclerView(toDoLists);

        btnInsertToDoList=findViewById(R.id.btnInsertToDoList);
        textInsertToDoList=findViewById(R.id.textInsertToDoList);

        btnInsertToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToDoList= textInsertToDoList.getText().toString();

                Toast toast=Toast.makeText(getApplicationContext(),nameToDoList,Toast.LENGTH_SHORT);
                toast.show();

                if (!nameToDoList.equals("")){
                    insertTodoList(nameToDoList);
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
    }

    public void buildToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void insertTodoList(String nameToDoList){
        toDoLists.add(new ToDoList(nameToDoList));
        toDoListAdapter.notifyItemInserted(toDoListAdapter.getItemCount()-1);
    }



}
