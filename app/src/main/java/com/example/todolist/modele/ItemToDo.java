package com.example.todolist.modele;

import java.io.Serializable;

/** Définition de la classe ItemToDo.
 * Cette classe représente un item (une tâche) d'une ToDoList
 */
public class ItemToDo implements Serializable {
    /* La description associée à l'item */
    private String description;
    /* Indique si l'item a été accompli ou non */
    private Boolean fait;
    /* Identifiant de l'item */
    private int id;

    /* Constructeur par défaut */
    public ItemToDo() {
        this.description = "";
        this.fait = false;
    }

    /** Constructeur par données
     * @param description la description à fournir à l'item
     */
    public ItemToDo(String description) {
        this.fait = false;
        this.description = description;
    }

    /** Constructeur par données
     * @param description la description à fournir à l'item
     * @param fait indique si l'item est accompli
     */
    public ItemToDo(String description, Boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    /** Constructeur par données
     * @param description la description à fournir à l'item
     * @param fait indique si l'item est accompli
     * @param id l'identifiant de l'item
     */
    public ItemToDo(String description, Boolean fait, int id) {
        this.description = description;
        this.fait = fait;
        this.id = id;
    }

    /** Accesseur de l'identifiant
     * @return l'identifiant associé à l'item
     */
    public int getId() {
        return id;
    }

    /** Accesseur de la description
     * @return la description associée à l'item
     */
    public String getDescription() {
        return description;
    }

    /** Mutateur de la description
     * @param uneDescription la description à associer à l'item
     */
    public void setDescription(String uneDescription) {
        this.description = uneDescription;
    }

    /** Accesseur du booléen
     * @return true si l'item a été accompli, false sinon
     */
    public Boolean isFait() {
        return fait;
    }

    /**
     * @return 1 si l'item est fait, 0 sinon
     */
    public int getFait(){
        if (fait)
            return 1;
        else
            return 0;
    }

    /** Mutateur du booléen
     * @param fait indique si l'item a été accompli ou non
     */
    public void setFait(Boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return description+" : "+fait;
    }
}
