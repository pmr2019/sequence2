package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

/** Définition de la classe UneListe.
 * Cette classe structure une ToDoList telle que conçue dans l'API, et est utilisée dans la classe
 *          Lists
 */
public class UneListe {

    /**
     * L'identifiant de la ToDoList
     */
    @SerializedName("id")
    public int id;

    /**
     * La titre (label) associé à la ToDoList
     */
    @SerializedName("label")
    public String titreListeToDO;
}
