package com.example.td_wang_yang_wei.DataClass;


import java.io.Serializable;

public class ItemToDo implements Serializable {
    private String description;
    private Boolean fait;

    public ItemToDo(String description, Boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    public ItemToDo(String description) {
        this.description = description;
        this.fait = Boolean.FALSE;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Boolean getFait() {
        return fait;
    }

    void setFait(Boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return ("Item : "+ this.getDescription() + " - Fait : " +this.getFait().toString());

    }

}
