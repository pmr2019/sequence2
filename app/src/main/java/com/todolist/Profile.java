package com.todolist;

import java.io.Serializable;
import java.util.ArrayList;

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

    public void ajouteListe(TodoList uneListe) {
        this.liste.add(uneListe);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "login='" + login + '\'' +
                ", liste=" + liste +
                '}';
    }
}
