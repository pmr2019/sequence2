package PMR.ToDoList.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;


public class Task {
    private String description;
    private Boolean fait;
    private UUID idTask;

    public Task(String description, Boolean fait) {
        this.description = description;
        this.fait = fait;
        this.idTask=UUID.randomUUID();
    }

    public Task(String description) {
        this.description = description;
        this.fait = Boolean.FALSE;
        this.idTask=UUID.randomUUID();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFait() {
        return fait;
    }

    public void setFait(Boolean fait) {
        this.fait = fait;
    }

    public UUID getIdTask() {
        return idTask;
    }

    @Override
    public String toString() {
        return ("Item : "+ this.getDescription() + " - Fait : " +this.getFait().toString());

    }

}
