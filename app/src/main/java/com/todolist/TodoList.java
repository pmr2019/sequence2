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

    public Item rechercherItem(String description) {
        for (Iterator i = this.liste.iterator(); i.hasNext(); ) {
            if (((Item) i).getDescription() == description) {
                return (Item) i;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "titreListeToDo='" + titreListeToDo + '\'' +
                "\nliste=" + liste +
                '}';
    }
}
