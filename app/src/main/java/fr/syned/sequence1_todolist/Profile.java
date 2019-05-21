package fr.syned.sequence1_todolist;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {

    private String username;
    private ArrayList<ToDoList> toDoLists;

    public Profile(String username) {
        this.username = username;
        this.toDoLists = new ArrayList<>();
    }

    public Profile (Profile profile) {
        this.username = profile.getUsername();
        this.toDoLists = profile.getToDoLists();
    }

    public void addToDoList(ToDoList toDoList) {
        this.toDoLists.add(toDoList);
    }

    public void addToDoList(String name) {
        this.toDoLists.add(new ToDoList(name));
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<ToDoList> getToDoLists() {
        return toDoLists;
    }

    public void setToDoLists(ArrayList<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }
}
