package PMR.ToDoList.Model;

import java.io.Serializable;


public class Task {
    private String description;
    private Boolean fait;

    public Task(String description, Boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    public Task(String description) {
        this.description = description;
        this.fait = Boolean.FALSE;
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

    @Override
    public String toString() {
        return ("Item : "+ this.getDescription() + " - Fait : " +this.getFait().toString());

    }

}
