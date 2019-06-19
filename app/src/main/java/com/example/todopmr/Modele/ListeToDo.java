package com.example.todopmr.Modele;

import com.example.todopmr.Modele.ItemToDo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
Classe de définition d'une todolist.
 */
public class ListeToDo implements Serializable {

    @SerializedName("label")
    private String titreListeToDo;

    @SerializedName("id")
    private int idListe;

    private ArrayList<ItemToDo> lesItems;

    private static int id0 = 0;

    /*
    Constructeur avec tous les arguments.
    */
    public ListeToDo(String titreListeToDo, ArrayList<ItemToDo> lesItems) {
        this.setTitreListeToDo(titreListeToDo);
        this.setLesItems(lesItems);
        this.idListe = id0 ++;
    }

    /*
    Constructeur avec le titre uniquement.
    */
    public ListeToDo(String titreListeToDo) {
        this(titreListeToDo, new ArrayList<ItemToDo>());
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

    /*
    Renvoie la valeur de l'identifiant de la liste.
    */
    public int getIdListe() {
        return this.idListe;
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
