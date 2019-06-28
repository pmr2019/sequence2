package com.example.todo.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.model.ListeToDo;

import java.util.List;

public class ItemAdapterList extends RecyclerView.Adapter<ItemAdapterList.ItemViewHolder> {

    private List<ListeToDo> items = null;
    private ActionListener actionListener;


    public ItemAdapterList(List<ListeToDo> list, ActionListener al) {
        this.actionListener = al;
        items = list;
    }

    public List<ListeToDo> getItems() {
        return items;
    }

    public void setItems(List<ListeToDo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_list, parent, false);
        Log.d("ItemAdapterList", "onCreateViewHolder() called with: parent = ["
                + parent
                + "], viewType = ["
                + viewType
                + "]");
        return new ItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListeToDo itemData = items.get(position);
        Log.d("ItemAdapterList",
                "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        holder.bind(itemData);


    }

    public void AddToList(ListeToDo list) {
        items.add(list);
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface ActionListener {
        public void onListClicked(Integer data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvList);
            imageView = itemView.findViewById(R.id.imgList);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        actionListener.onListClicked(items.get(getAdapterPosition()).getId());
                    }
                }
            });
        }

        public void bind(ListeToDo itemData) {
            textView.setText(itemData.getLabel());
            textView.setId(itemData.getId());
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

    }


}
