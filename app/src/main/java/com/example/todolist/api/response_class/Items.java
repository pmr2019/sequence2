package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Définition de la classe Items.
 * Cette classe permet de récupérer la réponse de l'API lors de la requête de demande
 *          d'une liste d'items
 */
public class Items {

    /**
     * La liste des items
     */
    @SerializedName("items")
    public List<UnItem> listeItems;

}
