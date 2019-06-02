package fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.syned.sequence1_todolist.Activities.ProfileActivity;
import fr.syned.sequence1_todolist.Activities.ToDoListActivity;
import fr.syned.sequence1_todolist.Model.Task;
import fr.syned.sequence1_todolist.Model.ToDoList;
import fr.syned.sequence1_todolist.R;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_PROFILE;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_UUID;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    private List<ToDoList> mDataset;

    private int removedPosition = 0;
    private ToDoList removedToDoList;

    public ToDoListAdapter(List<ToDoList> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_todolist, parent, false);
        return new ToDoListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ToDoListViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView title;
        private UUID uuid;
        private RecyclerView nestedRecyclerView;
        private TextView ellipsis;

        public ToDoListViewHolder(@NonNull View v) {
            super(v);

            cardView = v.findViewById(R.id.card_view);
            title = cardView.findViewById(R.id.title);
            nestedRecyclerView = v.findViewById(R.id.nested_recyclerview);
            ellipsis = cardView.findViewById(R.id.ellipsis);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                        v.getContext().startActivity(new Intent(v.getContext(), ToDoListActivity.class).putExtra(EXTRA_UUID, uuid));
                    }
                }
            });
        }

        public void bind(ToDoList toDoList) {
            title.setText(toDoList.getName());
            this.uuid = toDoList.getId();
            List<Task> tasks = new ArrayList<>();
            if (toDoList.getTasks().size() > 10) {
                for (int i = 0; i < 10; i++) {
                    tasks.add(toDoList.getTasks().get(i));
                }
            } else tasks = toDoList.getTasks();
            SubTaskAdapter subTaskAdapter = new SubTaskAdapter(tasks);
            nestedRecyclerView.setAdapter(subTaskAdapter);
            nestedRecyclerView.setLayoutManager(new LinearLayoutManager(nestedRecyclerView.getContext()));
            nestedRecyclerView.setLayoutFrozen(true);
            if (toDoList.getTasks().size() > 10) ellipsis.setVisibility(View.VISIBLE);
            else ellipsis.setVisibility(View.GONE);
        }

    }

    public void removeItem(RecyclerView.ViewHolder viewHolder) {
        removedPosition = viewHolder.getAdapterPosition();
        removedToDoList = mDataset.get(viewHolder.getAdapterPosition());

        mDataset.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());

        // TODO: Faire en sorte que la snackbar ne masque pas l'EditText et le FloatingActionButton
        Snackbar.make(viewHolder.itemView, removedToDoList.getName() + " deleted.",Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreLastItem(removedPosition, removedToDoList);
            }
        }).show();
    }

    public void restoreLastItem(int position, ToDoList toDoList) {
        mDataset.add(position, toDoList);
        notifyItemInserted(position);
    }
}