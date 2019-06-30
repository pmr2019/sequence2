package fr.syned.sequence1_todolist.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.syned.sequence1_todolist.activities.database.AppDatabase;
import fr.syned.sequence1_todolist.activities.database.User;
import fr.syned.sequence1_todolist.activities.recyclerview.SwipeItemTouchHelper;
import fr.syned.sequence1_todolist.activities.recyclerview.adapters.ToDoListAdapter;
import fr.syned.sequence1_todolist.model.Profile;
import fr.syned.sequence1_todolist.R;
import fr.syned.sequence1_todolist.model.ToDoList;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_HASH;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_ID;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_USERNAME;
import static fr.syned.sequence1_todolist.CustomApplication.database;
import static fr.syned.sequence1_todolist.CustomApplication.executor;
import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class ProfileActivity extends BaseActivity {

    EditText textViewToDoListName;
    private RecyclerView recyclerView;
    public static ToDoListAdapter toDoListAdapter;
    public static Profile profile;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username = getIntent().getSerializableExtra(EXTRA_USERNAME).toString();
        String hash = getIntent().getSerializableExtra(EXTRA_HASH).toString();
//        String id = getIntent().getStringExtra(EXTRA_ID);

//  SEQUENCE 1
//        Log.i("TAG", "onCreate: intent username: " + username);
//        for (Profile p : profilesList) {
//            if (p.getUsername().matches(username)) profile = p;
//        }
//
        profile = new Profile(username, hash, getApplicationContext());
        user = new User(profile);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.userDao().replaceAll(user);
            }
        });

        super.toolbar.setSubtitle(username);

        textViewToDoListName = findViewById(R.id.text_view_todolist_name);
        recyclerView = findViewById(R.id.todolists);
        toDoListAdapter = new ToDoListAdapter(profile.getToDoLists());
        recyclerView.setAdapter(toDoListAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
////        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if(!profilesList.contains(profile)) this.finish();
        // TODO: à faire seulement quand un changement a été fait... (Avec le StartActivityForResult)
        toDoListAdapter.notifyDataSetChanged();

    }

    public void onClickAddBtn(View view) {
//        if (!textViewToDoListName.getText().toString().matches("")) {
//            profile.addToDoList(textViewToDoListName.getText().toString());
//            toDoListAdapter.notifyDataSetChanged();
//            textViewToDoListName.setText(null);
//        }
    }

    public static void completeList(JSONArray items, String id, String hash) {
        profile.getList(id).addTasks(items, id, hash);
        toDoListAdapter.notifyDataSetChanged();
    }

    public static void completeProfile(JSONArray arr, String hash, Context c) throws JSONException {
        for(int i = 0; i < arr.length(); i++){
            profile.addToDoList((JSONObject)arr.get(i), hash, c);
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data != null) {
//
//        }
//    }

}
