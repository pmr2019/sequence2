package com.todolist;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Profile implements Serializable {
    private String login;
    private ArrayList<TodoList> liste;

    public Profile() {
        this.login = "Default";
        this.liste = new ArrayList<>();
    }

    public Profile(String login, ArrayList<TodoList> mesListeToDo) {
        super();
        this.login = login;
        this.liste = mesListeToDo;
    }

    public Profile(ArrayList<TodoList> mesListeToDo) {
        super();
        this.liste = mesListeToDo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ArrayList<TodoList> getListe() {
        return liste;
    }

    public void setListe(ArrayList<TodoList> liste) {
        this.liste = liste;
    }

    public void addList(TodoList uneListe) {
        this.liste.add(uneListe);
    }

    public TodoList getListByName(String list_name) {
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getTitreListeToDo().equals(list_name)) {
                return liste.get(i);
            }
        }
        return new TodoList();
    }

    public void addItem(String list_name, String item_name) {
        this.getListByName(list_name).addItem(new Item(item_name));
    }

    @Override
    public String toString() {
        return "Profile " + login;
    }
}
