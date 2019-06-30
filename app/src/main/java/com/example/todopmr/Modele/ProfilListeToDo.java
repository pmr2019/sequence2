package com.example.todopmr.Modele;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todopmr.Modele.ListeToDo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
Classe de définition d'un profil.
 */
@Entity
public class ProfilListeToDo implements Serializable {

    @Ignore
    private ArrayList<ListeToDo> mesListeToDo;

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "idProfil")
    private int idProfil;

    @SerializedName("pseudo")
    @ColumnInfo(name="pseudo")
    private String login;

    @Ignore
    private static int id0 = 0;

    /*
    Constructeur avec tous les arguments.
    */
    @Ignore
    public ProfilListeToDo(String login, ArrayList<ListeToDo> mesListeToDo) {
        this.setMesListeToDo(mesListeToDo);
        this.setLogin(login);
        this.idProfil = id0 ++;
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

    public int getIdProfil() {
        return idProfil;
    }

    public void setIdProfil(int id) {
        this.idProfil = id;
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + mesListeToDo + ", login=" + login + '}';
    }
}
