package com.example.todopmr.Modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
        (foreignKeys = @ForeignKey(entity = ListeToDo.class,
        parentColumns = "idListe",
        childColumns = "listeId")
 */

/*
Classe de définition d'un item.
 */
@Entity
public class ItemToDo implements Serializable {

    @SerializedName("label")
    @ColumnInfo(name = "label")
    private String description;

    @SerializedName("checked")
    @ColumnInfo(name = "checked")
    private int fait;

    @SerializedName("idItem")
    @PrimaryKey
    private int idItem;

    @ColumnInfo(name = "listeId")
    private int listeId;

    private static int id00 = 0;

    /*
    Constructeur avec tous les arguments.
    */
    @Ignore
    public ItemToDo(String description, int fait, int idListe, int id) {
        this.setDescription(description);
        this.setFait(fait);
        this.listeId = idListe;
        this.idItem = id;
    }

    @Ignore
    public ItemToDo(String description) {
        this.description = description;
    }

    @Ignore
    public ItemToDo(String description, int id) {
        this.idItem = id;
        this.description = description;
    }

    public ItemToDo() {
        super();
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

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int id) {
        this.idItem = id;
    }

    public int getListeId() {
        return listeId;
    }

    public void setListeId(int idListe) {
        this.listeId = idListe;
    }

    @Override
    public String toString() {
        return ("Identifiant : " + this.getIdItem() + " Description : " + this.getDescription() + " Fait : " +this.getFait());

    }
}
