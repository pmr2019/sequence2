package com.example.todo.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class ProfilListeToDo implements Serializable {

    @NonNull
    @PrimaryKey
    private String login;
    @Ignore
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
