package fr.syned.sequence1_todolist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ToDoList implements Serializable {

    private UUID id;
    private String name;
    private boolean isArchived;
    private ArrayList<Task> taskList;

    public ToDoList() {
        this.id = UUID.randomUUID();
        this.isArchived = false;
        this.taskList = new ArrayList<>();
    }

    public ToDoList(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void addTask(Task task) {
        this.taskList.add(task);
    }

    public void addTask(String name) {
        this.taskList.add(new Task(name));
    }

    public List<Task> getTasks() {
        return taskList;
    }
}
