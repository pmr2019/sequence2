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


public class PseudoItemSettings extends RecyclerView.Adapter<PseudoItemSettings.ItemSettingsViewHolder> {

    private final List<ProfilListeToDo> mList;

    private OnItemClickListener mListener;

    public void setOnClickListener(PseudoItemSettings.OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener{
        void onItemClick(int position, List<ProfilListeToDo> mList);
    }


    class ItemSettingsViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final TextView suppr;
        private final ImageView imageView;
        private final CheckBox checkBox;

        public ItemSettingsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            checkBox = itemView.findViewById(R.id.checkBox);
            suppr = itemView.findViewById(R.id.suppr);
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


    public PseudoItemSettings(List<ProfilListeToDo> item){
        this.mList=item;
    }

    @NonNull @Override
    public ItemSettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_settings,parent,false);
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
        return new ItemSettingsViewHolder(itemView, mListener);
    }


    public void onBindViewHolder(@NonNull ItemSettingsViewHolder holder, int position) {
        ProfilListeToDo itemData = mList.get(position);
        holder.textView.setText(itemData.getLogin());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.suppr.setText("Touchez pour Supprimer");
    }

    @Override public int getItemCount() {
        return mList.size();
    }
}
