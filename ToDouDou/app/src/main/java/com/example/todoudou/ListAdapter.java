package com.example.todoudou;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>{

    private final List<ListeToDo> mDataset;
    private final ActionListener actionListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final Button btn_delete;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.listText);
            btn_delete = v.findViewById(R.id.btn_delete);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        ListeToDo list = mDataset.get(getAdapterPosition());
                        actionListener.onItemClicked(list);
                    }
                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        ListeToDo list = mDataset.get(getAdapterPosition());
                        actionListener.onItemDelete(list);
                    }
                }
            });
        }

        public void bind(ListeToDo listData) {
            textView.setText(listData.getTitreListeToDo());
        }
    }



    public ListAdapter(ArrayList<ListeToDo> myDataset, ActionListener actionListener) {
        mDataset = myDataset;
        this.actionListener = actionListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listView = inflater.inflate(R.layout.list,parent,false);

        MyViewHolder vh = new MyViewHolder(listView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        if(mDataset!=null) return mDataset.size();
        else return 0;
    }


    interface ActionListener {
        void onItemClicked(ListeToDo data);
        void onItemDelete(ListeToDo data);
    }
}
