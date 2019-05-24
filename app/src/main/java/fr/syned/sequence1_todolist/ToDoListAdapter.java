package fr.syned.sequence1_todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    private List<ToDoList> mDataset;

    public static class ToDoListViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ToDoListViewHolder(@NonNull View v) {
            super(v);
            textView = v.findViewById(R.id.todolist_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Toast.makeText(v.getContext(), "Clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void bind(String item) {
            textView.setText(item);
        }
    }

    public ToDoListAdapter(List<ToDoList> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.todolist_item, parent, false);
        return new ToDoListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        holder.bind(mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}