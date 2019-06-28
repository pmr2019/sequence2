package com.ecl.maxime.application_todoliste.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.Item;

import java.util.ArrayList;

/**
 * Created by Max on 2019-05-23.
 */
public class ItemToDoAdapter extends RecyclerView.Adapter<ItemToDoAdapter.ItemViewHolder> {

    private ArrayList<Item> mLesItems;
    private final OnItemClickListener mListener;

    public ItemToDoAdapter(ArrayList<Item> lesItems, OnItemClickListener listener) {
        mLesItems = lesItems;
        mListener = listener;
    }

    public void setLesItems(ArrayList<Item> lesItems) {
        mLesItems = lesItems;
        notifyDataSetChanged();
    }

    public ArrayList<Item> getLesItems() {
        notifyDataSetChanged();
        return mLesItems;
    }

    @NonNull
    @Override
    public ItemToDoAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_todo,parent,false);
        return new ItemToDoAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemToDoAdapter.ItemViewHolder holder, int position) {
        Item itemToDo = mLesItems.get(position);
        holder.bind(itemToDo, mListener);
    }

    @Override
    public int getItemCount() {
        return mLesItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;
        private final CheckBox mCheckBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.description_item);
            mCheckBox = itemView.findViewById(R.id.box_fait);
        }

        public void bind(final Item itemToDo, final OnItemClickListener listener){
            mTextView.setText(itemToDo.getLabel());
            mCheckBox.setChecked(itemToDo.isChecked());
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemToDo);
                    }
                }
            );
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Item itemToDo);
    }
}
