package com.example.td_wang_yang_wei;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfilListeToDo implements Serializable {
    private List<ListeToDo> mesListeToDo;
    private String login;

    public ProfilListeToDo() {
        this.login="Default";
        this.mesListeToDo = new ArrayList<>();
    }


    public ProfilListeToDo(String login,List<ListeToDo> mesListeToDo) {
        super();
        this.mesListeToDo = mesListeToDo;
        this.login = login;
    }

    public ProfilListeToDo(List<ListeToDo> mesListeToDo) {
        super();
        this.mesListeToDo = mesListeToDo;
    }


    public ProfilListeToDo(String login) {
        super();
        this.login = login;
    }


    public List<ListeToDo> getMesListeToDo() {
        return mesListeToDo;
    }

    public void setMesListeToDo(List<ListeToDo> mesListeToDo) {
        this.mesListeToDo = mesListeToDo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void ajouteListe(ListeToDo uneListe)
    {
        this.mesListeToDo.add(uneListe);
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + mesListeToDo + ", login=" + login + '}';
    }


}
