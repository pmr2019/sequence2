package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

/**
 * Définition de la classe ItemResponse.
 * Cette classe permet de récupérer la réponse de l'API lors de la requête d'ajout d'un nouvel item
 */
public class ItemResponse {

    /**
     * L'item nouvellement créé
     */
    @SerializedName("item")
    public UnItem item;
}
