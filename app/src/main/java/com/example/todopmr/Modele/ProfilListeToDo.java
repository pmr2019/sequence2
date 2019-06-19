package com.example.todopmr.Modele;


import com.example.todopmr.Modele.ListeToDo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
Classe de définition d'un profil.
 */
public class ProfilListeToDo implements Serializable {

    private ArrayList<ListeToDo> mesListeToDo;

    @SerializedName("pseudo")
    private String login;

    /*
    Constructeur avec tous les arguments.
    */
    public ProfilListeToDo(String login, ArrayList<ListeToDo> mesListeToDo) {
        this.setMesListeToDo(mesListeToDo);
        this.setLogin(login);
    }

    /*
    Constructeur avec le pseudo uniquement.
     */
    public ProfilListeToDo(String login) {
        this(login, new ArrayList<ListeToDo>());
    }

    /*
    Renvoie la valeur de l'attribut mesListesToDo.
    */
    public ArrayList<ListeToDo> getMesListeToDo() {
        return mesListeToDo;
    }

    /*
    Modifie la valeur de l'attribut mesListesToDo.
    */
    public void setMesListeToDo(ArrayList<ListeToDo> mesListeToDo) {
        this.mesListeToDo = mesListeToDo;
    }

    /*
    Renvoie la valeur de l'attribut login.
    */
    public String getLogin() {
        return login;
    }

    /*
    Modifie la valeur de l'attribut login.
    */
    public void setLogin(String login) {
        this.login = login;
    }

    /*
    Ajoute une liste donnée en paramètre à l'attribut mesListesToDo.
     */
    public void ajouteListe(ListeToDo uneListe) {
        this.mesListeToDo.add(uneListe);
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + mesListeToDo + ", login=" + login + '}';
    }
}
