package com.example.todolist;

import java.util.ArrayList;
import java.util.List;

public class ProfilListeToDo {
    public String login;
    public List<ListeToDo> mesListToDo;

    public ProfilListeToDo(){
    }

    public ProfilListeToDo(String login){
        this.login = login;
        this.mesListToDo = new ArrayList<>();
    }

    public ProfilListeToDo(List<ListeToDo> mesListToDo) {
        this.mesListToDo = mesListToDo;
    }

    public ProfilListeToDo(String login, List<ListeToDo> mesListToDo) {
        this.login = login;
        this.mesListToDo = mesListToDo;
    }

    public String getLogin() {
        return login;
    }

    public List<ListeToDo> getMesListToDo() {
        return mesListToDo;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMesListToDo(List<ListeToDo> mesListToDo) {
        this.mesListToDo = mesListToDo;
    }

    public void ajouteList(ListeToDo uneListe){
        this.mesListToDo.add(uneListe);
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" +
                "login='" + login + '\'' +
                ", mesListToDo=" + mesListToDo +
                '}';
    }
}
