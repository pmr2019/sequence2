package fr.syned.sequence1_todolist.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.SwipeToDelete.SwipeItemTouchHelper;
import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.TaskAdapter;
import fr.syned.sequence1_todolist.Model.ToDoList;
import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.Activities.ProfileActivity.profile;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_ID;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_UUID;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class ToDoListActivity extends BaseActivity {

    public static ToDoList toDoList;
    EditText textViewTaskName;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private String hash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoList = profile.getToDoList((UUID) getIntent().getSerializableExtra(EXTRA_UUID));
        hash = toDoList.getHash();

        Log.i("TAG", "onCreate, toDoList: " + toDoList);

        super.toolbar.setSubtitle(toDoList.getName());

        textViewTaskName = findViewById(R.id.text_view_task_name);
        recyclerView = findViewById(R.id.tasks);
        taskAdapter = new TaskAdapter(toDoList.getTasks());
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_todolist;
    }

    public void onClickAddBtn(View view) {
        if (!textViewTaskName.getText().toString().matches("")) {
            toDoList.addTask(textViewTaskName.getText().toString());
            taskAdapter.notifyDataSetChanged();
            textViewTaskName.setText(null);
        }
    }
    @Override
    public void onResume() {

        super.onResume();
//        if(!profilesList.contains(profile)) this.finish();
    }
}
