package PMR.ToDoList.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.R;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {

    private ArrayList<ToDoList> toDoLists;

    public ToDoListAdapter(ArrayList<ToDoList> toDoLists){

        this.toDoLists=toDoLists;
    }

    @NonNull
    @Override
    public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todolist,parent,false);
        ToDoListViewHolder tdlv=new ToDoListViewHolder(v);
        return tdlv;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
        ToDoList toDoList = toDoLists.get(position);
        holder.nameToDoList.setText(toDoList.getNameToDoList());
    }

    @Override
    public int getItemCount() {
        return toDoLists.size();
    }

    public static class ToDoListViewHolder extends RecyclerView.ViewHolder{

        public TextView nameToDoList;

        public ToDoListViewHolder(@NonNull View itemView) {
            super(itemView);
            nameToDoList = itemView.findViewById(R.id.textTodoList);
        }
    }

}
