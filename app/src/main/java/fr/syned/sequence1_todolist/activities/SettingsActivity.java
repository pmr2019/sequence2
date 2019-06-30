package fr.syned.sequence1_todolist.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.syned.sequence1_todolist.activities.recyclerview.adapters.ProfileAdapter;
import fr.syned.sequence1_todolist.activities.recyclerview.SwipeItemTouchHelper;
import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.CustomApplication.profilesList;

public class SettingsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
