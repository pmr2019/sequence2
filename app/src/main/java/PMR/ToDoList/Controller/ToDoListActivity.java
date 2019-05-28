package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    private ToDoListAdapter toDoListAdapter;
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

        //BOUTON D'INSERTION D'UNE TO DO LIST
        btnInsertToDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToDoList= textInsertToDoList.getText().toString();
                textInsertToDoList.setText("");

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


        toDoListAdapter.setOnItemClickListener(new ToDoListAdapter.OnItemClickListener() {
            @Override
            //BOUTON QUAND ON CLIQUE SUR UNE CARD
            public void onItemClick(int position) {
                //On récupère la TODOLIST en question
                accessTasksActivity(position);
            }
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


    public void insertTodoList(String nameToDoList){
        toDoLists.add(new ToDoList(nameToDoList));
        toDoListAdapter.notifyItemInserted(toDoListAdapter.getItemCount()-1);
    }



    // FONCTION QUI FAIT DES TRUCS QUAND ON CLIQUE SUR UNE TO DO LIST
    //ELLE PREND EN ARGUMENT LA POSITION DE LA TO DO LIST DANS LA LISTE DES TO DO LISTS

    public void accessTasksActivity(int position){
        //toDoLists.get(position).setTitreListeToDo("clicked");
        //toDoListAdapter.notifyItemChanged(position);

        Intent intent=new Intent(ToDoListActivity.this,TasksActivity.class);
        startActivity(intent);
    }

}
