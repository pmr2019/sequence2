package com.example.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.Item;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final List<Item> mItemList;
    private final ActionListener mActionListener;

    // ------------------------------------------------------------------------------------------ //
    public ItemsAdapter(List<Item> itemList, ActionListener actionListener) {
        mItemList = itemList;
        mActionListener = actionListener;
    }

    // ------------------------------------------------------------------------------------------ //
    public interface ActionListener {
        void onCheckedClicked(int position, boolean isChecked);
        void onDeleteClicked(int position);
    }

    // ------------------------------------------------------------------------------------------ //
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View choiceItemView = inflater.inflate(R.layout.recycler_item, parent, false);

        return new ItemViewHolder(choiceItemView);
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder holder, int position) {
        String itemData = mItemList.get(position).getDescription();
        int checked = mItemList.get(position).getDone();
        holder.bind(itemData, checked);
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    // ------------------------------------------------------------------------------------------ //
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mItemTV;
        private final CheckBox mItemCB;
        private final ImageView mItemDeleteI;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemTV = itemView.findViewById(R.id.itemTextView);
            mItemCB = itemView.findViewById(R.id.itemCheckBox);
            mItemDeleteI = itemView.findViewById(R.id.itemDelete);

            // listener quand on coche un listener
            mItemCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mActionListener.onCheckedClicked(position, isChecked);
                    }
                }
            });

            // listener quand on appuye sur l'icone de suppression
            mItemDeleteI.setOnClickListener(new View.OnClickListener() {
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
        private void bind(String itemDescription, int checked) {
            mItemTV.setText(itemDescription);
            mItemCB.setChecked(checked == 1);
        }
    }
}