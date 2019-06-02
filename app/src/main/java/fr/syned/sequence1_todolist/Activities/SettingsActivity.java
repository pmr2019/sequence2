package fr.syned.sequence1_todolist.Activities;

import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.ProfileAdapter;
import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.SwipeToDelete.SwipeItemTouchHelper;
import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.ToDoListAdapter;
import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class SettingsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        recyclerView = findViewById(R.id.profiles);
        profileAdapter = new ProfileAdapter(profilesList);
        recyclerView.setAdapter(profileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_settings;
    }
}
