package PMR.ToDoList.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import PMR.ToDoList.R;
import PMR.ToDoList.View.TaskView;
import PMR.ToDoList.View.ToDoListView;

public class ToDoListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ToDoListView> toDoListList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_list);

        toDoListList=new ArrayList<>();
        toDoListList.add(new ToDoListView("Todo1"));
        toDoListList.add(new ToDoListView("Todo2"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));
        toDoListList.add(new ToDoListView("Todo3"));

        mRecyclerView = findViewById(R.id.rvTodoList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ToDoListAdapter(toDoListList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
