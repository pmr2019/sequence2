package com.example.todo.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "lists")
public class ListeToDo implements Serializable {
    private String label;
    @PrimaryKey private Integer id;


    public ListeToDo(String label, Integer id) {
        this.label = label;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ListeToDo{" +
                "label='" + label + '\'' +
                ", id=" + id +
                '}';
    }
}
