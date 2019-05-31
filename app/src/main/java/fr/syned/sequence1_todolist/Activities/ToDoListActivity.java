package fr.syned.sequence1_todolist.Activities;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.UUID;

import fr.syned.sequence1_todolist.R;
import fr.syned.sequence1_todolist.RecyclerViewAdapters.TaskAdapter;
import fr.syned.sequence1_todolist.Model.ToDoList;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_UUID;
import static fr.syned.sequence1_todolist.Activities.ProfileActivity.profile;

public class ToDoListActivity extends BaseActivity {

    public static ToDoList toDoList;
    EditText textViewTaskName;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_todolist);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.btn_add_task);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        toDoList = (ToDoList) getIntent().getSerializableExtra(EXTRA_TODOLIST);
        toDoList = profile.getToDoList((UUID) getIntent().getSerializableExtra(EXTRA_UUID));
        Log.i("TAG", "onCreate, toDoList: " + toDoList);

        super.toolbar.setSubtitle(toDoList.getName());

        textViewTaskName = findViewById(R.id.text_view_task_name);
        recyclerView = findViewById(R.id.tasks);
        taskAdapter = new TaskAdapter(toDoList.getTasks());
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_todolist;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void onClickAddBtn(View view) {
        if (!textViewTaskName.getText().toString().matches("")) {
            toDoList.addTask(textViewTaskName.getText().toString());
            taskAdapter.notifyDataSetChanged();
            textViewTaskName.setText(null);
//            Intent intent = new Intent(this, ProfileActivity.class);
//            intent.putExtra(EXTRA_TODOLIST, toDoList);
//            setResult(Activity.RESULT_OK, intent);
        }
    }
}
