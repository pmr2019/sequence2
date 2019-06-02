package fr.syned.sequence1_todolist.Activities.RecyclerViewAdapters;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.syned.sequence1_todolist.Model.Task;
import fr.syned.sequence1_todolist.R;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder> {
    private List<Task> mDataset;


    public SubTaskAdapter(List<Task> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public SubTaskViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.subitem_task, parent, false);
        return new SubTaskAdapter.SubTaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class SubTaskViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public CheckBox checkBox;

        public SubTaskViewHolder(@NonNull View v) {
            super(v);
            textView = v.findViewById(R.id.text);
            checkBox = v.findViewById(R.id.checkbox);
        }

        public void bind(Task task) {
            textView.setText(task.getName());
            Log.i("TAG", "bind: " + task.isDone());
            checkBox.setChecked(task.isDone());
            if (task.isDone()) textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
