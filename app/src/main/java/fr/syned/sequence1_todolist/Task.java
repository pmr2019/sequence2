package fr.syned.sequence1_todolist;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Task implements Serializable {

    private int id;
    private String name;
    private String description;
    private String label;
    private boolean isDone;
    private Date creationDate;
    private Date doneDate;

    public Task() {
        this.id = 0;
        this.isDone = false;
        this.creationDate = Calendar.getInstance().getTime();
    }

    public Task(String name){
        this();
        this.name = name;
    }
}
