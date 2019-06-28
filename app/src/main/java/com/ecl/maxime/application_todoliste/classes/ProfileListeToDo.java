package com.ecl.maxime.application_todoliste.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Max on 2019-05-19.
 */

@Entity
public class ProfileListeToDo implements Serializable {

    @PrimaryKey
    private String login;
    @PrimaryKey
    private String mdp;
    protected ArrayList<ListeToDo> mMesListeToDo;

    public ProfileListeToDo(String login, String mdp, ArrayList<ListeToDo> mesListeToDo) {
        this.login = login;
        this.mdp = mdp;
        mMesListeToDo = mesListeToDo;
    }
    public ProfileListeToDo(String login, String mdp) {
        this.login = login;
        this.mdp = mdp;

    }
    public ProfileListeToDo(ArrayList<ListeToDo> mesListeToDo) {
        mMesListeToDo = mesListeToDo;
    }

    public ProfileListeToDo() {
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ArrayList<ListeToDo> getMesListeToDo() {
        return mMesListeToDo;
    }

    public void setMesListeToDo(ArrayList<ListeToDo> mesListeToDo) {
        mMesListeToDo = mesListeToDo;
    }

    public void ajouteListe(ListeToDo uneListe){
        mMesListeToDo.add(uneListe);
    }

    @Override
    public String toString() {
        return "ProfileListeToDo{" +
                "login='" + login + '\'' +
                ", mMesListeToDo=" + mMesListeToDo +
                '}';
    }
}
