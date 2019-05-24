package fr.syned.sequence1_todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> mDataset;

    public TaskAdapter(List<Task> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.task_item, parent, false);
        return new TaskAdapter.TaskViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(mDataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public TaskViewHolder(@NonNull View v, final Context mContext) {
            super(v);
            textView = v.findViewById(R.id.task_name);
        }

        public void bind(String item) {
            textView.setText(item);
        }
    }
}
