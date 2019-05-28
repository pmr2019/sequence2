package PMR.ToDoList.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import PMR.ToDoList.R;
import PMR.ToDoList.View.ToDoListView;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {

    private ArrayList<ToDoListView> toDoLists;

    public ToDoListAdapter(ArrayList<ToDoListView> toDoLists){

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
        ToDoListView todoListView = toDoLists.get(position);
        holder.textToDoList.setText(todoListView.getNomTodoList());
    }

    @Override
    public int getItemCount() {
        return toDoLists.size();
    }

    public static class ToDoListViewHolder extends RecyclerView.ViewHolder{

        public TextView textToDoList;

        public ToDoListViewHolder(@NonNull View itemView) {
            super(itemView);
            textToDoList = itemView.findViewById(R.id.textTodoList);
        }
    }

}
