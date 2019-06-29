package PMR.ToDoList.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import PMR.ToDoList.data.Model.ToDoList;
import PMR.ToDoList.R;

/*
Adapter lié au recyclerview utilisé dans l'activité To Do List
 */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {

    private ArrayList<ToDoList> toDoLists;
    private OnItemClickListener toDoListListener;

    public ToDoListAdapter(ArrayList<ToDoList> toDoLists){

        this.toDoLists=toDoLists;
    }

    // INTERFACE ONITEMCLICKLISTENER POUR ECOUTER A L'INTERIEUR

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        toDoListListener=listener;
    }

    // METHODES A IMPLEMENTER DANS l'INTERFACE D'UN ADAPTER

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todolist,parent,false);
        ToDoListViewHolder tdlv=new ToDoListViewHolder(v, toDoListListener);
        return tdlv;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        ToDoList toDoList = toDoLists.get(position);
        holder.nameToDoList.setText(toDoList.getLabel());
    }

    @Override
    public int getItemCount() {
        return toDoLists.size();
    }



    // CLASS TODOLISTVIEWHOLDER

    public static class ToDoListViewHolder extends RecyclerView.ViewHolder{

        public TextView nameToDoList;

        public ToDoListViewHolder(@NonNull View itemView, final OnItemClickListener toDoListListener) {
            super(itemView);
            nameToDoList = itemView.findViewById(R.id.textTodoList);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toDoListListener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            toDoListListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
