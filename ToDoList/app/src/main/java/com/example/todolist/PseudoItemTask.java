package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class PseudoItemTask extends RecyclerView.Adapter<PseudoItemTask.ItemTaskViewHolder> {

    private final List<ItemToDo> mList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onCheckBoxClick(int position);
        void onItemClick(int position, List<ItemToDo> mList);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    class ItemTaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;
        private final CheckBox checkBox;

        public ItemTaskViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position, mList);
                        }
                    }
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onCheckBoxClick(position);
                        }
                    }
                }
            });
        }
    }


    public PseudoItemTask(List<ItemToDo> item){
        this.mList=item;
    }

    @NonNull @Override
    public ItemTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_task,parent,false);
        /*
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(itemView);
                String item = mList.get(itemPosition);
                Toast.makeText(parent.getContext(), mList, Toast.LENGTH_LONG).show();
            }
        });
        */
        return new ItemTaskViewHolder(itemView, mListener);
    }


    public void onBindViewHolder(@NonNull ItemTaskViewHolder holder, int position) {
        ItemToDo itemData = mList.get(position);
        holder.textView.setText(itemData.getDesciption());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.checkBox.setChecked(itemData.getFait());
    }

    @Override public int getItemCount() {

        return mList.size();
    }
}
