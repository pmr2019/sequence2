package fr.syned.sequence1_todolist;

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

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public boolean toggleCheckbox() {
        this.isDone = !isDone;
        return isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
