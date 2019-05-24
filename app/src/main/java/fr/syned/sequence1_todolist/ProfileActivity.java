package fr.syned.sequence1_todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static fr.syned.sequence1_todolist.MainActivity.EXTRA_PROFILE;
import static fr.syned.sequence1_todolist.MainActivity.EXTRA_TODOLIST;

public class ProfileActivity extends AppCompatActivity {

    EditText textViewToDoListName;
    private RecyclerView recyclerView;
    private ToDoListAdapter toDoListAdapter;
    static public Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profile = (Profile) getIntent().getSerializableExtra(EXTRA_PROFILE);

        textViewToDoListName = findViewById(R.id.text_view_todolist_name);
        recyclerView = findViewById(R.id.todolists);
        toDoListAdapter = new ToDoListAdapter(profile.getToDoLists());
        recyclerView.setAdapter(toDoListAdapter);
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
        if (!textViewToDoListName.getText().toString().matches("")) {
            profile.addToDoList(textViewToDoListName.getText().toString());
            toDoListAdapter.notifyDataSetChanged();
            textViewToDoListName.setText(null);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_PROFILE, profile);
            setResult(Activity.RESULT_OK, intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            ToDoList returnedToDoList = (ToDoList) data.getSerializableExtra(EXTRA_TODOLIST);
            profile.replaceToDoList(profile.getToDoList(returnedToDoList.getId()), returnedToDoList);
            toDoListAdapter.notifyDataSetChanged();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_PROFILE, profile);
            setResult(Activity.RESULT_OK, intent);
        }
    }
}
