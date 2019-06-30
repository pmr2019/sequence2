package PMR.ToDoList.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import PMR.ToDoList.data.Model.Task;
import PMR.ToDoList.R;

/*
Adapter lié au recyclerview utilisé dans l'activité Tsk
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private ArrayList<Task> tasks;
    private OnItemClickListener taskListener;


    public TasksAdapter(ArrayList<Task> tasks){

        this.tasks=tasks;
    }

    // INTERFACE ONITEMCLICKLISTENER POUR ECOUTER A L'INTERIEUR

    public interface OnItemClickListener {
        void onCheckBoxClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        taskListener=listener;
    }

    //METHODES A IMPLEMENTER DANS UN ADAPTER

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
        holder.nameTask.setText(task.getLabelTask());
        if (task.getChecked()==1) holder.checkBoxTask.setChecked(Boolean.TRUE);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // CLASS TODOLISTVIEWHOLDER

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTask;
        public CheckBox checkBoxTask;

        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener taskListener) {
            super(itemView);
            nameTask = itemView.findViewById(R.id.textTask);
            checkBoxTask=itemView.findViewById(R.id.checkBoxTask);

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
