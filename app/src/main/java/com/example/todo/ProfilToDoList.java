package com.example.todo;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfilToDoList implements Serializable {
    private List<ListeToDo> mesListeToDo=null;
    private String login;

    public ProfilToDoList(List<ListeToDo> mesListeToDo, String login) {
        this.mesListeToDo = mesListeToDo;
        this.login = login;
    }

    public ProfilToDoList(String login) {
        this.login = login;
        this.mesListeToDo = new ArrayList<>();
    }

    public ProfilToDoList() {
        this.mesListeToDo = new ArrayList<>();
    }

    public ProfilToDoList(List<ListeToDo> mesListeToDo) {
        this.mesListeToDo = mesListeToDo;
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
