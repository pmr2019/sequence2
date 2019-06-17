package com.example.todo.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfilListeToDo implements Serializable {
    private String login;
    private ArrayList<ListeToDo> mesListeToDo = new ArrayList<ListeToDo>();

    public ProfilListeToDo() {
    }

    public ProfilListeToDo(ArrayList<ListeToDo> mesListeToDo) {
        this.mesListeToDo = mesListeToDo;
    }

    public ProfilListeToDo(String login, ArrayList<ListeToDo> mesListeToDo) {
        this.login = login;
        this.mesListeToDo = mesListeToDo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ArrayList<ListeToDo> getMesListeToDo() {
        return mesListeToDo;
    }

    public void setMesListeToDo(ArrayList<ListeToDo> mesListeToDo) {
        this.mesListeToDo = mesListeToDo;
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" +
                "login='" + login + '\'' +
                ", mesListeToDo=" + mesListeToDo +
                '}';
    }

    /**
     * Add one ListeToDo to this profile.
     * @param uneListe
     */
    public void ajouteListe(ListeToDo uneListe){
        this.mesListeToDo.add(uneListe);
    }
}
