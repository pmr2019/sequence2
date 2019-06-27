package fr.syned.sequence1_todolist.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Task implements Serializable {

    private UUID id;
    private String name;
    private String description;
    private String label;
    private boolean isDone;
    private Date creationDate;
    private Date doneDate;

    public Task() {
        this.id = UUID.randomUUID();
        this.isDone = false;
        this.creationDate = Calendar.getInstance().getTime();
    }

    public Task(String name){
        this();
        this.name = name;
    }

    public Task(String taskName, String checked) {
        this(taskName);
        this.isDone = checked.equals("1");
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public boolean toggleCheckbox() {
        this.isDone = !isDone;
        if (isDone) doneDate = Calendar.getInstance().getTime();
        else doneDate = null;
        return isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
