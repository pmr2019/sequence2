package com.example.todo.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(
        onDelete = CASCADE,
        entity = ProfilListeToDo.class,
        parentColumns = "login",
        childColumns = "profilListeToDoId"))
public class ListeToDo implements Serializable {

    @PrimaryKey
    private int id;
    private String profilListeToDoId;
    private String titreListeToDo;
    @Ignore
    private ArrayList<ItemToDo> lesItems = new ArrayList<ItemToDo>();

    public ListeToDo() {
    }

    public ListeToDo(String profilListeToDoId, String titreListeToDo) {
        this.profilListeToDoId = profilListeToDoId;
        this.titreListeToDo = titreListeToDo;
    }

    public ListeToDo(int id, String profilListeToDoId, String titreListeToDo, ArrayList<ItemToDo> lesItems) {
        this.id = id;
        this.profilListeToDoId = profilListeToDoId;
        this.titreListeToDo = titreListeToDo;
        this.lesItems = lesItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfilListeToDoId() {
        return profilListeToDoId;
    }

    public void setProfilListeToDoId(String profilListeToDoId) {
        this.profilListeToDoId = profilListeToDoId;
    }

    public String getTitreListeToDo() {
        return titreListeToDo;
    }

    public void setTitreListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
    }

    public ArrayList<ItemToDo> getLesItems() {
        return lesItems;
    }

    public void setLesItems(ArrayList<ItemToDo> lesItems) {
        this.lesItems = lesItems;
    }

    @Override
    public String toString() {
        return "ListeToDo{" +
                "id=" + id +
                ", profilListeToDoId=" + profilListeToDoId +
                ", titreListeToDo='" + titreListeToDo + '\'' +
                ", lesItems=" + lesItems +
                '}';
    }

    public ItemToDo rechercherItem (String descriptionItem){
        for (ItemToDo i: this.lesItems){
            if (i.getDescription().contains(descriptionItem)){
                return i;
            }
        }
        return null;
    }

    public void ajouteItem(ItemToDo item){
        this.lesItems.add(item);
    }
}
