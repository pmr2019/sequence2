package com.example.todolist;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ProfilListeToDo.class,
        parentColumns = "idUser",
        childColumns = "idUserAssocie"))
public class ListeToDo {

    @PrimaryKey
    private int idList;
    private int idUserAssocie;
    private String titreListeToDo;

    @Ignore
    public ListeToDo() {
    }

    @Ignore
    public ListeToDo(String titreListeToDo, int idList){
        this.titreListeToDo=titreListeToDo;
        this.idList=idList;
    }

    public ListeToDo(String titreListeToDo,int idList ,int idUserAssocie){
        this.titreListeToDo = titreListeToDo;
        this.idList = idList;
        this.idUserAssocie = idUserAssocie;
    }



    public int getIdUserAssocie() {
        return idUserAssocie;
    }
    public int getIdList() {
        return idList;
    }
    public String getTitreListeToDo() {

        return titreListeToDo;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }
    public void setIdUserAssocie(int idUserAssocie) {
        this.idUserAssocie = idUserAssocie;
    }
    public void setTitreListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
    }

    @Override
    public String toString() {
        return "ListeToDo{" +
                "id='" + idList + '\'' +
                ", titreListeToDo='" + titreListeToDo + '\'' +
                '}';
    }
}
