package fr.syned.sequence1_todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_PROFILE;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_TODOLIST;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_UUID;
import static fr.syned.sequence1_todolist.CustomApplication.PICK_CONTACT_REQUEST;
import static fr.syned.sequence1_todolist.ProfileActivity.profile;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    private List<ToDoList> mDataset;

    public ToDoListAdapter(List<ToDoList> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_todolist, parent, false);
        return new ToDoListViewHolder(v, parent.getContext());
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

        private TextView textView;
        private UUID uuid;

        public ToDoListViewHolder(@NonNull View v, final Context mContext) {
            super(v);
            textView = v.findViewById(R.id.todolist_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION) {
//                        Toast.makeText(v.getContext(), "Clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(v.getContext(), ToDoListActivity.class);
//                        ToDoList selectedToDoList;
//                        selectedToDoList = ProfileActivity.profile.getToDoList(uuid);
//                        intent.putExtra(EXTRA_TODOLIST, selectedToDoList);
//                        ((Activity) mContext).startActivityForResult(intent, PICK_CONTACT_REQUEST);
                        v.getContext().startActivity(new Intent(v.getContext(), ToDoListActivity.class).putExtra(EXTRA_UUID, uuid).putExtra(EXTRA_PROFILE, ((ProfileActivity)v.getContext()).profile));
                    }
                }
            });
        }

        public void bind(ToDoList toDoList) {
            textView.setText(toDoList.getName());
            this.uuid = toDoList.getId();
        }
    }

}