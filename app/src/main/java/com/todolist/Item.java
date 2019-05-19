package com.todolist;

import java.io.Serializable;

public class Item implements Serializable {
    private String description;
    private boolean fait;

    public Item() {
        this.description = "Default";
        this.fait = false;
    }

    public Item(String description) {
        this.fait = false;
        this.description = description;
    }

    public Item(String description, boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getFait() {
        return fait;
    }

    public void setFait(boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return "Item{" +
                "description='" + description + '\'' +
                ", fait=" + fait +
                '}';
    }
}
