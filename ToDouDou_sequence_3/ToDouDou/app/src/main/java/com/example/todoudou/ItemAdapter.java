package com.example.todoudou;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.todoudou.database.ItemToDo;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private final List<ItemToDo> mDataset;
    private final ActionListener actionListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final CheckBox checkBox;
        private final Button btn_delete;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
            checkBox = v.findViewById(R.id.checkBox);
            btn_delete = v.findViewById(R.id.btn_delete_show);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        ItemToDo item = mDataset.get(getAdapterPosition());
                        actionListener.onItemChecked(item);
                    }
                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        ItemToDo item = mDataset.get(getAdapterPosition());
                        actionListener.onItemDelete(item);
                    }
                }
            });
        }

        public void bind(ItemToDo itemData) {
            textView.setText(itemData.getDescription());
            checkBox.setChecked(itemData.getFait());
        }
    }



    public ItemAdapter(ArrayList<ItemToDo> myDataset, ActionListener actionListener) {
        mDataset = myDataset;
        this.actionListener = actionListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item,parent,false);

        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    interface ActionListener {
        void onItemChecked(ItemToDo data);
        void onItemDelete(ItemToDo data);
    }
}

