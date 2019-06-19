package com.example.todopmr.Modele;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
Classe de définition d'un item.
 */
public class ItemToDo implements Serializable {

    @SerializedName("label")
    private String description;

    @SerializedName("checked")
    private int fait;

    @SerializedName("id")
    private int idItem;

    private static int id00 = 0;

    /*
    Constructeur avec tous les arguments.
    */
    public ItemToDo(String description, int fait) {
        this.setDescription(description);
        this.setFait(fait);
        this.idItem= id00 ++;
    }

    /*
    Constructeur avec la description uniquement.
    */
    public ItemToDo(String description) {
        this(description, 0);
    }

    /*
    Renvoie la valeur de l'attribut description.
    */
    public String getDescription() {
        return description;
    }

    /*
    Modifie la valeur de l'attribut description.
    */
    public void setDescription(String description) {
        this.description = description;
    }

    /*
    Renvoie la valeur de l'attribut fait.
    */
    public int getFait() {
        return fait;
    }

    /*
    Modifie la valeur de l'attribut fait.
    */
    public void setFait(int fait) {
        this.fait = fait;
    }

    /*
    Renvoie la valeur de l'identifiant de l'item.
    */
    public int getIdItem() {
        return idItem;
    }

    @Override
    public String toString() {
        return ("Identifiant : " + this.getIdItem() + " Description : " + this.getDescription() + " Fait : " +this.getFait());

    }
}
