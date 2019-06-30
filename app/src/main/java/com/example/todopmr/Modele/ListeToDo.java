package com.example.todopmr.Modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
Classe de définition d'une todolist.
 */
@Entity
/*
(foreignKeys = @ForeignKey(entity = ProfilListeToDo.class,
        parentColumns = "idProfil",
        childColumns = "profilId")
 */

public class ListeToDo implements Serializable {

    @SerializedName("label")
    @ColumnInfo(name = "label")
    private String titreListeToDo;

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "idListe")
    private int idListe;

    @ColumnInfo(name = "profilId")
    private int profilId;

    @Ignore
    private ArrayList<ItemToDo> lesItems;

    private static int id0 = 0;

    /*
    Constructeur avec tous les arguments.
    */
    public ListeToDo(String titreListeToDo, ArrayList<ItemToDo> lesItems, int profilId, int id) {
        this.setTitreListeToDo(titreListeToDo);
        this.setLesItems(lesItems);
        this.profilId = profilId;
        this.idListe = id;
    }

    /*
    Constructeur avec le titre uniquement (et l'identifiant de l'utilisateur).
    */
    @Ignore
    public ListeToDo(String titreListeToDo, int profilId, int listeId) {
        this(titreListeToDo, new ArrayList<ItemToDo>(), profilId, listeId);
    }

    public ListeToDo() {
        super();
    }

    /*
    Renvoie la valeur de l'attribut titreListeToDo.
    */
    public String getTitreListeToDo() {
        return this.titreListeToDo;
    }

    /*
    Modifie la valeur de l'attribut titreListeToDo.
    */
    public void setTitreListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
    }

    /*
    Renvoie la valeur de l'attribut lesItems.
    */
    public ArrayList<ItemToDo> getLesItems() {
        return this.lesItems;
    }

    /*
    Modifie la valeur de l'attribut lesItems.
    */
    public void setLesItems(ArrayList<ItemToDo> lesItems) {
        this.lesItems = lesItems;
    }

    public int getIdListe() {
        return idListe;
    }

    public void setIdListe(int id) {
        this.idListe = id;
    }

    public int getProfilId() {
        return profilId;
    }

    public void setProfilId(int profilId) {
        this.profilId = profilId;
    }

    /*
                Ajoute un item donné en paramètre à l'attribut lesItems.
                 */
    public void ajouterItem(ItemToDo unItem) {
        this.lesItems.add(unItem);
    }

    /* FONCTION NON UTILISEE : rechercherItem

    public int rechercherItem(String s) {
        int retour = -1;
        Boolean trouve = Boolean.FALSE;
        for (int i=0; i < this.lesItems.size() ;i++) {
            if (this.lesItems.get(i).getDescription().equals(s)) {
                retour=i;
                i=this.lesItems.size();
                trouve=Boolean.TRUE;
            }
        }
        return retour;
    }
    */

    @Override
    public String toString() {
        String retour;
        retour = "Identifiant : " + this.getIdListe() + " Liste : " + this.getTitreListeToDo()+ " Items : " + this.getLesItems().toString();
        return retour;
    }

}
