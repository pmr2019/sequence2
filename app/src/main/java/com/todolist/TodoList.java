package com.todolist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class TodoList implements Serializable {
    private String titreListeToDo;
    private ArrayList<Item> liste;

    public TodoList() {
        this.titreListeToDo = "Default";
        this.liste = new ArrayList<>();
    }

    public TodoList(String titre) {
        this.liste = new ArrayList<>();
        this.titreListeToDo = titre;
    }

    public String getTitreListeToDo() {
        return titreListeToDo;
    }

    public void setTitreListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
    }

    public void setLesItems(ArrayList<Item> items) {
        this.liste = items;
    }

    public ArrayList<Item> getLesItems() {
        return this.liste;
    }

    public void addItem(Item i) {
        this.liste.add(i);
    }

    public void setItemStatus(String item_selected, boolean isChecked){
        for (Item i : liste) {
            if (i.getDescription().equals(item_selected)){
                i.setFait(isChecked);
            }
        }
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "titreListeToDo='" + titreListeToDo + '\'' +
                "\nliste=" + liste +
                '}';
    }
}
