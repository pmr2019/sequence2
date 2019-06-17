package com.example.todo.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ListeToDo implements Serializable {
    private String titreListeToDo;
    private ArrayList<ItemToDo> lesItems = new ArrayList<ItemToDo>();

    public ListeToDo() {
    }

    public ListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
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
                "titreListeToDo='" + titreListeToDo + '\'' +
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
