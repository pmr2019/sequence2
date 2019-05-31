package fr.syned.sequence1_todolist.Activities;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import fr.syned.sequence1_todolist.Model.Profile;
import fr.syned.sequence1_todolist.R;
import fr.syned.sequence1_todolist.RecyclerViewAdapters.ToDoListAdapter;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_USERNAME;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class ProfileActivity extends BaseActivity {

    EditText textViewToDoListName;
    private RecyclerView recyclerView;
    private ToDoListAdapter toDoListAdapter;
    static public Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        profile = (Profile) getIntent().getSerializableExtra(EXTRA_PROFILE);
        String username = getIntent().getSerializableExtra(EXTRA_USERNAME).toString();
        Log.i("TAG", "onCreate: intent username: " + username);
        for (Profile p : profilesList) {
            if (p.getUsername().matches(username)) profile = p;
        }

        super.toolbar.setSubtitle(username);

        textViewToDoListName = findViewById(R.id.text_view_todolist_name);
        recyclerView = findViewById(R.id.todolists);
        toDoListAdapter = new ToDoListAdapter(profile.getToDoLists());
        recyclerView.setAdapter(toDoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
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
        if (!textViewToDoListName.getText().toString().matches("")) {
            profile.addToDoList(textViewToDoListName.getText().toString());
            toDoListAdapter.notifyDataSetChanged();
            textViewToDoListName.setText(null);
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra(EXTRA_PROFILE, profile);
//            setResult(Activity.RESULT_OK, intent);
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data != null) {
//            ToDoList returnedToDoList = (ToDoList) data.getSerializableExtra(EXTRA_TODOLIST);
//            profile.replaceToDoList(profile.getToDoList(returnedToDoList.getId()), returnedToDoList);
//            toDoListAdapter.notifyDataSetChanged();
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra(EXTRA_PROFILE, profile);
//            setResult(Activity.RESULT_OK, intent);
//        }
//    }
}
