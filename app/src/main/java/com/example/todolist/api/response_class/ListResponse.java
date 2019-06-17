package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

/** Définition de la classe ListResponse.
 * Cette classe permet de récupérer la réponse de l'API lors de la requête d'ajout d'une nouvelle
 *          ToDoList
 */
public class ListResponse {

    /**
     * La nouvelle ToDoList nouvellement créée
     */
    @SerializedName("list")
    public UneListe list;
}
