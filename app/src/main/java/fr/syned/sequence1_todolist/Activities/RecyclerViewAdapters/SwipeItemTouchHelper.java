package fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import fr.syned.sequence1_todolist.Model.ToDoList;

public class SwipeItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerView recyclerView;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     */
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
    }
}
