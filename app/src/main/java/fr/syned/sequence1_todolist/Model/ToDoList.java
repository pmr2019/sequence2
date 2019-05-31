package fr.syned.sequence1_todolist.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ToDoList implements Serializable {

    private UUID id;
    private String name;
    private boolean isArchived;
    private ArrayList<Task> taskList;

    private transient HashMap<UUID, Task> taskMap;

    public ToDoList() {
        this.id = UUID.randomUUID();
        this.isArchived = false;
        this.taskList = new ArrayList<>();
        onDeserialization();
    }

    public ToDoList(String name) {
        this();
        this.name = name;
    }

    public void onDeserialization() {
        taskMap = new HashMap<>();
        for (Task t : taskList) {
            taskMap.put(t.getId(), t);
        }
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void addTask(Task task) {
        this.taskList.add(task);
        this.taskMap.put(task.getId(), task);
    }

    public void addTask(String name) {
        Task task = new Task(name);
        this.taskList.add(task);
        this.taskMap.put(task.getId(), task);
    }

    public Task getTask(UUID taskId) {
        return taskMap.get(taskId);
    }

    public void replaceTask(Task oldTask, Task newTask) {
        Collections.replaceAll(taskList, oldTask, newTask);
        taskMap.remove(oldTask.getId());
        taskMap.put(newTask.getId(), newTask);
    }

    public List<Task> getTasks() {
        return taskList;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        onDeserialization();
    }
}
