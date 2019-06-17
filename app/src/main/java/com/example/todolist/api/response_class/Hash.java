package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

/** Définition de la classe Hash.
 * Cette classe permet de récupérer la réponse de l'API lors de la requête de demande
 *          de connexion
 */
public class Hash {

    /**
     * Le hash d'identification récupéré
     */
    @SerializedName("hash")
    public String hash;
}
