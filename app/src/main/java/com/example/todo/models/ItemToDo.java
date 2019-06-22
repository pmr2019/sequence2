package com.example.todo.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(entity = ProfilListeToDo.class,
        parentColumns = "id",
        childColumns = "listeToDoId"))
public class ItemToDo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int listeToDoId;

    private String description;
    private boolean fait=false;

    public ItemToDo() {
    }

    public ItemToDo(String description) {
        this.description = description;
    }

    public ItemToDo(String description, boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListeToDoId() {
        return listeToDoId;
    }

    public void setListeToDoId(int listeToDoId) {
        this.listeToDoId = listeToDoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFait() {
        return fait;
    }

    public void setFait(boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return "ItemToDo{" +
                "id=" + id +
                ", listeToDoId=" + listeToDoId +
                ", description='" + description + '\'' +
                ", fait=" + fait +
                '}';
    }
}
