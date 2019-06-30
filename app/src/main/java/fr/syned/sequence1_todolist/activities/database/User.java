package fr.syned.sequence1_todolist.activities.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import fr.syned.sequence1_todolist.model.Profile;
import fr.syned.sequence1_todolist.model.ToDoList;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "todolists")
    private String toDoLists;


    public User(String username) {
        this.username = username;
    }

    public User(Profile profile) {
        this.username = profile.getUsername();
//        this.uid = profile.getId();
        Gson gson = new GsonBuilder().create();
        this.toDoLists = gson.toJson(getToDoLists());
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToDoLists() {
        return toDoLists;
    }

    public void setToDoLists(String toDoLists) {
        this.toDoLists = toDoLists;
    }

}