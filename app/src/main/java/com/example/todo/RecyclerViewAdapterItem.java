package com.example.todo;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.todo.models.ItemToDo;

import java.util.ArrayList;

public class RecyclerViewAdapterItem extends RecyclerView.Adapter<RecyclerViewAdapterItem.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapterItem";

    private Context mContext;
    private ArrayList<ItemToDo> mItem = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkboxItem;
        Button btnDelete;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxItem = itemView.findViewById(R.id.checkboxItem);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public RecyclerViewAdapterItem(Context Context, ArrayList<ItemToDo> items) {
        this.mContext = Context;
        this.mItem = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        final CheckBox checkboxItem = holder.checkboxItem;
        checkboxItem.setText(mItem.get(i).getDescription());
        if(mContext instanceof ShowListActivity){
            if (mItem.get(i).isFait()){
                checkboxItem.setChecked(true);
                checkboxItem.setPaintFlags(checkboxItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //permet de rayer le texte de l'item
            } else {
                checkboxItem.setChecked(false);
                checkboxItem.setPaintFlags(0);
            }
        }
        checkboxItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on : "+mItem.get(i).getDescription());
                if(mContext instanceof ShowListActivity){
                    if (checkboxItem.isChecked())
                        ((ShowListActivity)mContext).checkItem(i, true);
                    else
                        ((ShowListActivity)mContext).checkItem(i, false);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShowListActivity)mContext).delItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

}
