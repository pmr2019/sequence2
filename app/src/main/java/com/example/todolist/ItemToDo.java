package com.example.todolist;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ListeToDo.class,
        parentColumns = "idList",
        childColumns = "idListAssocie"))
public class ItemToDo {
    @PrimaryKey
    private int idItem;
    private int idListAssocie;
    private String desciption;
    private Boolean fait;
    private Boolean modifie=false;

    @Ignore
    public ItemToDo(){
    }

    @Ignore
    public ItemToDo(String desciption, Boolean fait){
        this.desciption = desciption;
        this.fait=false;
    }

    public ItemToDo(int idItem, String desciption, Boolean fait) {
        this.idItem = idItem;
        this.desciption = desciption;
        this.fait=fait;
    }

    @Ignore
    public ItemToDo(int idItem, String desciption, String fait, int idListAssocie) {
        this.idItem = idItem;
        this.desciption = desciption;
        this.idListAssocie = idListAssocie;
        if (fait.matches("0")){
            this.fait=false;
        }
        else {
            this.fait=true;
        }
    }

    public int getIdListAssocie() {
        return idListAssocie;
    }
    public int getIdItem() {
        return idItem;
    }
    public String getDesciption() {
        return desciption;
    }
    public Boolean getFait() {
        return fait;
    }
    public Boolean getModifie() {
        return modifie;
    }


    public void setModifie(Boolean modifie) {
        this.modifie = modifie;
    }
    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
    public void setFait(Boolean fait) {
        this.fait = fait;
    }
    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }
    public void setIdListAssocie(int idListAssocie) {
        this.idListAssocie = idListAssocie;
    }

    @Override
    public String toString() {
        return "ItemToDo{" +
                "desciption='" + desciption + '\'' +
                ", fait=" + fait +
                '}';
    }
}
