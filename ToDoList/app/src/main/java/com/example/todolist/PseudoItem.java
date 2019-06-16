package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class PseudoItem extends RecyclerView.Adapter<PseudoItem.ItemViewHolder> {

    private final List<ListeToDo> mList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position, List<ListeToDo> mList);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;

        public ItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
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
        }
    }


    public PseudoItem(List<ListeToDo> item){

        this.mList=item;
    }

    @NonNull @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item,parent,false);
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
        return new ItemViewHolder(itemView, mListener);
    }

    @Override public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListeToDo itemData = mList.get(position);
        holder.textView.setText(itemData.getTitreListeToDo());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override public int getItemCount() {
        //return 1;
        return mList.size();
    }
    }
