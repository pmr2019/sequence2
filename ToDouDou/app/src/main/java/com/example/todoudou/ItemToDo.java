package com.example.todoudou;


import java.io.Serializable;

public class ItemToDo implements Serializable {
    private String description;
    private Boolean fait;
    private int id = 0;

    public ItemToDo(String description, Boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    public ItemToDo(String description) {
        this.description = description;
        this.fait = Boolean.FALSE;
    }

    public ItemToDo(String description, int id) {
        this.description = description;
        this.fait = Boolean.FALSE;
        this.id = id;
    }
    public ItemToDo(String description, Boolean fait, int id) {
        this.description = description;
        this.fait = fait;
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFait() {
        return fait;
    }

    public void setFait(Boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return ("Item : "+ this.getDescription() + " - Fait : " +this.getFait().toString());

    }

}