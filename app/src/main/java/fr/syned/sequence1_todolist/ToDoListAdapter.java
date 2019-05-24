package fr.syned.sequence1_todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    private List<ToDoList> mDataset;

    public ToDoListAdapter(List<ToDoList> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.todolist_item, parent, false);
        return new ToDoListViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        holder.bind(mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ToDoListViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ToDoListViewHolder(@NonNull View v, final Context mContext) {
            super(v);
            textView = v.findViewById(R.id.todolist_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Toast.makeText(v.getContext(), "Clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), ToDoListActivity.class);
                        ToDoList selectedToDoList;
                        String toDoListName = textView.getText().toString();
                        selectedToDoList = ProfileActivity.profile.getToDoList(toDoListName);
                        intent.putExtra(MainActivity.EXTRA_TODOLIST, selectedToDoList);
                        ((Activity) mContext).startActivityForResult(intent, MainActivity.PICK_CONTACT_REQUEST);
                    }
                }
            });
        }

        public void bind(String item) {
            textView.setText(item);
        }
    }

}