package com.example.todo.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.model.ItemToDo;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ItemAdapterItem extends RecyclerView.Adapter<ItemAdapterItem.ItemViewHolder> {

    private List<ItemToDo> items = null;
    private ActionListener actionListener;


    private ItemToDo recentlyDeletedItem;
    private int recentlyDeletedItemPosition;
    private Activity mActivity;

    public ItemAdapterItem(List<ItemToDo> list, ActionListener al) {
        this.actionListener = al;
        items = list;
    }

    public List<ItemToDo> getItems() {
        return items;
    }

    public void setItems(List<ItemToDo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mActivity = (Activity) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_item, parent, false);
        Log.d("ItemAdapterList", "onCreateViewHolder() called with: parent = ["
                + parent
                + "], viewType = ["
                + viewType
                + "]");
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemToDo itemData = items.get(position);
        Log.d("ItemAdapterList",
                "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        holder.bind(itemData);

    }

    public void AddToList(ItemToDo item) {
        items.add(item);
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    public void addToPosition(ItemToDo item, Integer position) {
        items.add(position, item);
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Deleting and restoring
    public void deleteItem(int position) {
        recentlyDeletedItem = items.get(position);
        recentlyDeletedItemPosition = position;
        items.remove(position);
        notifyItemRemoved(position);
        actionListener.onItemRemoved(recentlyDeletedItem.getId());
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.itemsLayout);
        Snackbar snackbar = Snackbar.make(view, "Are you sure ?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        actionListener.onUndoDelete(recentlyDeletedItem.getLabel(),recentlyDeletedItem.getChecked(), recentlyDeletedItemPosition);
    }

    public interface ActionListener {
        public void onCheckBoxClicked(Integer idItem, int state);

        public void onItemRemoved(Integer id);

        public void onUndoDelete(String label,Integer State, Integer position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvItem);
            checkBox = itemView.findViewById(R.id.checkBoxItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        //Click on item
                    }
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Integer stateInteger = !checkBox.isChecked() ? 1 : 0;
                        //change visually
                        ItemToDo modifiedItem = items.get(getAdapterPosition());
                        modifiedItem.setChecked(stateInteger);

                        //save changes
                        int idItem = items.get(getAdapterPosition()).getId();
                        actionListener.onCheckBoxClicked(idItem, checkBox.isChecked() ? 1 : 0);

                    }
                }
            });


        }


        public void bind(ItemToDo itemData) {
            textView.setText(itemData.getLabel());
            textView.setId(itemData.getId());
            checkBox.setChecked(itemData.getChecked() == 1);

        }
    }

}
