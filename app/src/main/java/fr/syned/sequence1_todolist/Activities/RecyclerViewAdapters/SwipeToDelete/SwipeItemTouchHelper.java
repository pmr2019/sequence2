package fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.SwipeToDelete;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.ProfileAdapter;
import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.TaskAdapter;
import fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters.ToDoListAdapter;

public class SwipeItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerView recyclerView;

    public SwipeItemTouchHelper(int dragDirs, int swipeDirs, RecyclerView recyclerView) {
        super(dragDirs, swipeDirs);
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        Toast.makeText(recyclerView.getContext(), "on Move", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Toast.makeText(recyclerView.getContext(), "on Swiped ", Toast.LENGTH_SHORT).show();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(adapter instanceof ToDoListAdapter) ((ToDoListAdapter) adapter).removeItem(viewHolder);
        if(adapter instanceof TaskAdapter) ((TaskAdapter) adapter).removeItem(viewHolder);
        if(adapter instanceof ProfileAdapter) ((ProfileAdapter) adapter).removeItem(viewHolder);
    }
}
