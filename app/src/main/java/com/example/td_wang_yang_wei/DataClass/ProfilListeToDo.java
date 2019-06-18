package com.example.td_wang_yang_wei.DataClass;

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

    public void addItem(String liste, ItemToDo item){
        for (int i=0; i < this.mesListeToDo.size() ;i++)
        {
            if (mesListeToDo.get(i).getTitreListeToDo().equals(liste))
            {
                this.mesListeToDo.get(i).ajouterItem(item);
            }
        }

    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void ajouteListe(ListeToDo uneListe)
    {
        this.mesListeToDo.add(uneListe);
    }

    public ListeToDo rechercherListe(String liste) {
        for (int i=0; i < this.mesListeToDo.size() ;i++)
        {
            if (this.mesListeToDo.get(i).getTitreListeToDo().equals(liste))
            {
                return mesListeToDo.get(i);
            }
        }
        return new ListeToDo();
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + mesListeToDo + ", login=" + login + '}';
    }


}
