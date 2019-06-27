package com.example.todolist.modele;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Définition de la classe ProfilListeToDo.
 * Cette classe représente un profil (utilisateur) de notre application
 */
public class ProfilListeToDo implements Serializable {
    /* Le pseudo de l'utilisateur */
    private String login;
    /* La liste des ToDoLists auxquelles est inscrit l'utilisateur */
    private ArrayList<ListeToDo> mesListesToDo;

    /* Constructeur par défaut */
    public ProfilListeToDo() {
        this.login = "";
        this.mesListesToDo = new ArrayList<>();
    }

    /**
     * Constructeur par données
     *
     * @param login         le pseudo de l'utilisateur
     * @param mesListesToDo la liste des ToDoLists associée à l'utilisateur
     */
    public ProfilListeToDo(String login, ArrayList<ListeToDo> mesListesToDo) {
        this.login = login;
        this.mesListesToDo = mesListesToDo;
    }

    /**
     * Constructeur par données
     *
     * @param mesListesToDo la liste des ToDoLists associée à l'utilisateur
     */
    public ProfilListeToDo(ArrayList<ListeToDo> mesListesToDo) {
        this.login = "";
        this.mesListesToDo = mesListesToDo;
    }

    /**
     * Accesseur du pseudo
     *
     * @return le pseudo associé à l'utilisateur
     */
    public String getLogin() {
        return login;
    }

    /**
     * Mutateur du pseudo
     *
     * @param unLogin le pseudo à associer à l'utilisateur
     */
    public void setLogin(String unLogin) {
        this.login = unLogin;
    }

    /**
     * Accesseur de la liste des ToDoLists
     *
     * @return les ToDoList associées à l'utilisateur
     */
    public ArrayList<ListeToDo> getMesListesToDo() {
        return mesListesToDo;
    }

    /**
     * Mutateur de la liste des ToDoLists
     *
     * @param mesListesToDo les ToDoLists à associer à l'utilisateur
     */
    public void setMesListesToDo(ArrayList<ListeToDo> mesListesToDo) {
        this.mesListesToDo = mesListesToDo;
    }

    /**
     * Ajout d'une ToDoList à la liste des ToDoLists de l'utilisateur
     *
     * @return true si l'ajout s'est bien effectué, false sinon
     */
    public boolean ajouteListe(ListeToDo uneListe) {
        return mesListesToDo.add(uneListe);
    }

    @Override
    public String toString() {
        String res = login + " : [ ";
        for (int i = 0; i < mesListesToDo.size(); i++) {
            res += mesListesToDo.get(i).toString();
        }
        res += " ]\n";
        return res;
    }
}
