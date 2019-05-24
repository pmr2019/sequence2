package fr.syned.sequence1_todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import static fr.syned.sequence1_todolist.MainActivity.EXTRA_PROFILE;
import static fr.syned.sequence1_todolist.MainActivity.EXTRA_TODOLIST;

public class ToDoListActivity extends AppCompatActivity {

    private ToDoList toDoList;
    EditText textViewTaskName;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.btn_add_task);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        toDoList = (ToDoList) getIntent().getSerializableExtra(EXTRA_TODOLIST);
        Log.i("TAG", "onCreate, toDoList: " + toDoList);

        textViewTaskName = findViewById(R.id.text_view_task_name);
        recyclerView = findViewById(R.id.tasks);
        taskAdapter = new TaskAdapter(toDoList.getTasks());
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickAddBtn(View view) {
        if (!textViewTaskName.getText().toString().matches("")) {
            toDoList.addTask(textViewTaskName.getText().toString());
            taskAdapter.notifyDataSetChanged();
            textViewTaskName.setText(null);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(EXTRA_TODOLIST, toDoList);
            setResult(Activity.RESULT_OK, intent);
        }
    }
}
