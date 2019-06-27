package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Définition de la classe Lists.
 * Cette classe permet de récupérer la réponse de l'API lors de la requête de demande
 * d'une liste de ToDoLists
 */
public class Lists {

    /**
     * La liste des ToDoLists
     */
    @SerializedName("lists")
    public List<UneListe> listeDeListes;

}
