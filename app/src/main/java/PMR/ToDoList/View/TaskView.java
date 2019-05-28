package PMR.ToDoList.View;

public class TaskView {

    private String nomTask;

    public TaskView(String nomTask){

        this.nomTask=nomTask;
    }

    public String getNomTask() {
        return nomTask;
    }

    public void setNomTask(String nomTask) {
        this.nomTask = nomTask;
    }
}
