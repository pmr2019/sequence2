package fr.syned.sequence1_todolist.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.syned.sequence1_todolist.Activities.ProfileActivity;
import fr.syned.sequence1_todolist.Activities.ToDoListActivity;
import fr.syned.sequence1_todolist.Model.Task;
import fr.syned.sequence1_todolist.R;
import fr.syned.sequence1_todolist.Model.ToDoList;

import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_PROFILE;
import static fr.syned.sequence1_todolist.CustomApplication.EXTRA_UUID;

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
        ArrayList<View> subList = new ArrayList<>();
        TextView ellipsis;

        public ToDoListViewHolder(@NonNull View v) {
            super(v);

            cardView = v.findViewById(R.id.card_view);
            title = cardView.findViewById(R.id.title);

            subList.add(cardView.findViewById(R.id.sub0));
            subList.add(cardView.findViewById(R.id.sub1));
            subList.add(cardView.findViewById(R.id.sub2));
            subList.add(cardView.findViewById(R.id.sub3));
            subList.add(cardView.findViewById(R.id.sub4));
            subList.add(cardView.findViewById(R.id.sub5));
            subList.add(cardView.findViewById(R.id.sub6));
            subList.add(cardView.findViewById(R.id.sub7));
            subList.add(cardView.findViewById(R.id.sub8));
            subList.add(cardView.findViewById(R.id.sub9));

            ellipsis = cardView.findViewById(R.id.ellipsis);

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
            title.setText(toDoList.getName());
            this.uuid = toDoList.getId();
            List<Task> tasks = toDoList.getTasks();
            TextView taskName;
            CheckBox checkBox;
            View sub;
            for (int i = 0; i < Math.min(tasks.size(), 10); i++) {
                sub = subList.get(i);
                taskName = sub.findViewById(R.id.text);
                taskName.setText(tasks.get(i).getName());
                checkBox = sub.findViewById(R.id.checkbox);
                checkBox.setChecked(tasks.get(i).isDone());
                if (tasks.get(i).isDone()) taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                else taskName.setPaintFlags(taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                sub.setVisibility(View.VISIBLE);
            }
            if (tasks.size() > 10) ellipsis.setVisibility(View.VISIBLE);
        }
    }

}