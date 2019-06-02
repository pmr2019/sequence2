package fr.syned.sequence1_todolist.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile implements Serializable {

    private String username;
    private ArrayList<ToDoList> toDoLists;

    private transient Map<UUID, ToDoList> toDoListMap;

    public Profile(String username) {
        this.username = username;
        this.toDoLists = new ArrayList<>();
        onDeserialization();
    }

    public void onDeserialization() {
        toDoListMap = new HashMap<>();
        for (ToDoList tdl : toDoLists) {
            toDoListMap.put(tdl.getId(), tdl);
        }
    }

    public void addToDoList(String name) {
        ToDoList toDoList = new ToDoList(name);
        this.toDoLists.add(toDoList);
        this.toDoListMap.put(toDoList.getId(), toDoList);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<ToDoList> getToDoLists() {
        return toDoLists;
    }


    public ToDoList getToDoList(UUID toDoListId) {
        return toDoListMap.get(toDoListId);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        onDeserialization();
    }
}
