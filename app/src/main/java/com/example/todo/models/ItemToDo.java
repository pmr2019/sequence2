package com.example.todo.models;

import java.io.Serializable;

public class ItemToDo implements Serializable {
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
                "description='" + description + '\'' +
                ", fait=" + fait +
                '}';
    }
}
