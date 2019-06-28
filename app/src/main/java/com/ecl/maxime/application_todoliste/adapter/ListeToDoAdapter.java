package com.ecl.maxime.application_todoliste.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecl.maxime.application_todoliste.R;
import com.ecl.maxime.application_todoliste.api_request.Liste;

import java.util.ArrayList;

/**
 * Created by Max on 2019-05-19.
 */
public class ListeToDoAdapter extends RecyclerView.Adapter<ListeToDoAdapter.ListItemViewHolder> {

    private ArrayList<Liste> mLesListes;
    private final OnItemClickListener mListener;

    public ListeToDoAdapter(ArrayList<Liste> lesListes, OnItemClickListener listener) {
        mLesListes = lesListes;
        mListener = listener;
    }

    public void setLesListes(ArrayList<Liste> lesListes) {
        mLesListes = lesListes;
        notifyDataSetChanged();
    }

    public ArrayList<Liste> getLesListes() {
        return mLesListes;
    }

    @NonNull
    @Override
    public ListeToDoAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listeItemView = inflater.inflate(R.layout.item_liste,parent,false);
        return new ListItemViewHolder(listeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListeToDoAdapter.ListItemViewHolder holder, int position) {
        Liste listeToDo = mLesListes.get(position);
        holder.bind(listeToDo, mListener, position);
    }

    @Override
    public int getItemCount() {
        return mLesListes.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.title_liste);
        }

        public void bind(final Liste listeToDo, final OnItemClickListener listener, final int position){
            mTextView.setText(listeToDo.getLabel());
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(listeToDo, position);

                }
            });
        }
    }

    public interface OnItemClickListener{

        void onItemClick(Liste listeToDo, int i);
    }
}
