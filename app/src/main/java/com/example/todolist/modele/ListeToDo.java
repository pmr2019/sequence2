package com.example.todolist.modele;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Définition de la classe ListeToDo.
 * Cette classe représente une ToDoList
 */
public class ListeToDo implements Serializable {
    /* Titre de la ToDoList */
    private String titreListeToDo;
    /* Liste des items de la ToDoList */
    private ArrayList<ItemToDo> lesItems;
    /* L'identifiant de la ToDoList */
    private int id;

    /* Constructeur par défaut */
    public ListeToDo() {
        this.titreListeToDo = "";
        this.lesItems = new ArrayList<>();
    }

    /**
     * Constructeur par données
     *
     * @param titreListeToDo le titre de la ToDoList
     * @param id             l'identiiant de la ToDoList
     */
    public ListeToDo(String titreListeToDo, int id) {
        this.titreListeToDo = titreListeToDo;
        this.lesItems = new ArrayList<>();
        this.id = id;

    }

    /**
     * Accesseur de l'identifiant
     *
     * @return l'identifiant associé à la ToDoList
     */
    public int getId() {
        return id;
    }

    /**
     * Accesseur du titre
     *
     * @return le titre associé à la ToDoList
     */
    public String getTitreListeToDo() {
        return titreListeToDo;
    }

    /**
     * Mutateur du titre
     *
     * @param titre le titre à associer à la ToDoList
     */
    public void setTitreListeToDo(String titre) {
        this.titreListeToDo = titre;
    }

    /**
     * Accesseur de la liste des items
     *
     * @return la liste des items associée à la ToDoList
     */
    public ArrayList<ItemToDo> getLesItems() {
        return lesItems;
    }

    /**
     * Mutateur de la liste des items
     *
     * @param lesItems la liste des items à associer à la ToDoList
     */
    public void setLesItems(ArrayList<ItemToDo> lesItems) {
        this.lesItems = lesItems;
    }

    /**
     * Recherche d'un item par sa description dans la liste des items associée à la ToDoList.
     *
     * @param descriptionItem la description à rechercher
     * @return l'indice où se trouve l'item dans la liste (-1 si l'item n'existe pas)
     * La recherche est insensible à la casse
     */
    public int rechercherItem(String descriptionItem) {
        for (int i = 0; i < lesItems.size(); i++) {
            if (lesItems.get(i).getDescription().equalsIgnoreCase(descriptionItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ajout d'un item à la liste des items associée à la ToDoList
     *
     * @param item l'item à ajouter
     * @return true si l'ajout s'est bien effectué, false sinon
     */
    public boolean ajouterItem(ItemToDo item) {
        return lesItems.add(item);
    }

    @Override
    public String toString() {
        String res = titreListeToDo + " : [ ";
        for (int i = 0; i < lesItems.size(); i++) {
            res += lesItems.get(i).toString() + ", ";
        }
        res = res.substring(0, res.length() - 2);
        res += " ]\n";
        return res;
    }
}
