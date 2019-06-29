package com.example.todo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.todo.activities.ChoixListActivity;
import com.example.todo.R;
import com.example.todo.activities.ShowListActivity;
import com.example.todo.models.ListeToDo;

import java.util.ArrayList;

public class RecyclerViewAdapterList extends RecyclerView.Adapter<RecyclerViewAdapterList.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapterList";

    private Context mContext;
    private ArrayList<ListeToDo> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView list_name;
        Button btnDelete;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public RecyclerViewAdapterList(Context Context, ArrayList<ListeToDo> lists) {
        this.mContext = Context;
        this.mList = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listlist, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.list_name.setText(mList.get(i).getTitreListeToDo());
        holder.list_name.setOnClickListener(v -> {
            Intent toAct = new Intent(mContext, ShowListActivity.class);
            Bundle data = new Bundle();
            data.putInt("idList", mList.get(i).getId());
            data.putString("pseudo", mList.get(i).getProfilListeToDoId());
            toAct.putExtras(data);
            mContext.startActivity(toAct);
        });
        holder.btnDelete.setOnClickListener(v -> ((ChoixListActivity)mContext).delListeToDo(mList.get(i), i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
