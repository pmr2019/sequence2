package PMR.ToDoList.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import PMR.ToDoList.Model.Task;
import PMR.ToDoList.R;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private ArrayList<Task> tasks;
    private OnItemClickListener taskListener;


    public TasksAdapter(ArrayList<Task> tasks){

        this.tasks=tasks;
    }

    // INTERFACE ONITEMCLICKLISTENER POUR ECOUTER A L'INTERIEUR

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onCheckBoxClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        taskListener=listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
        TaskViewHolder tdlv=new TaskViewHolder(v, taskListener);
        return tdlv;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.nameTask.setText(task.getDescription());
        holder.checkBoxTask.setChecked(task.getFait());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // CLASS TODOLISTVIEWHOLDER

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTask;
        public ImageView btnDeleteTask;
        public CheckBox checkBoxTask;

        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener taskListener) {
            super(itemView);
            nameTask = itemView.findViewById(R.id.textTask);
            btnDeleteTask=itemView.findViewById(R.id.btnDeleteTask);
            checkBoxTask=itemView.findViewById(R.id.checkBoxTask);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskListener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            taskListener.onItemClick(position);
                        }
                    }
                }
            });

            btnDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskListener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            taskListener.onDeleteClick(position);
                        }
                    }
                }
            });

            checkBoxTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskListener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            taskListener.onCheckBoxClick(position);
                        }
                    }
                }
            });

        }
    }
}
