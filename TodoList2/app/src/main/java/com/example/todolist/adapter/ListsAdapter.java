package com.example.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.TodoList;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ItemViewHolder> {

    private final List<TodoList> mTodoList;
    private final ActionListener mActionListener;

    // ------------------------------------------------------------------------------------------ //
    public ListsAdapter(List<TodoList> todoLists, ActionListener actionListener) {
        mTodoList = todoLists;
        mActionListener = actionListener;
    }

    // ------------------------------------------------------------------------------------------ //
    public interface ActionListener {
        void onItemClicked(int idList);
        void onDeleteClicked(int position);
    }

    // ------------------------------------------------------------------------------------------ //
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listView = inflater.inflate(R.layout.recycler_todo_list, parent, false);

        return new ItemViewHolder(listView);
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String listTitle = mTodoList.get(position).getTitle();
        holder.bind(listTitle);
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    // ------------------------------------------------------------------------------------------ //
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mListTV;
        private final ImageView mListDeleteI;

        // -------------------------------------------------------------------------------------- //
        ItemViewHolder(@NonNull View listView) {
            super(listView);
            mListTV = listView.findViewById(R.id.listTextView);
            mListDeleteI = listView.findViewById(R.id.listDelete);

            // listener quand on appuye sur une liste
            listView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mActionListener.onItemClicked(mTodoList.get(position).getId());
                    }
                }
            });

            // listener quand on appuye sur l'icone de suppression
            mListDeleteI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mActionListener.onDeleteClicked(position);
                    }
                }
            });
        }

        // -------------------------------------------------------------------------------------- //
        private void bind(String listTitle) {
            mListTV.setText(listTitle);
        }
    }
}
