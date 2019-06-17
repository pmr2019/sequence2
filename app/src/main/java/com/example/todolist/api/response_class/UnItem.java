package com.example.todolist.api.response_class;

import com.google.gson.annotations.SerializedName;

/** Définition de la classe UnItem.
 * Cette classe structure un item tel que conçu dans l'API, et est utilisée dans la classe Items
 */
public class UnItem {

    /**
     * L'identifiant de l'item
     */
    @SerializedName("id")
    public int id;

    /**
     * Le label de l'item
     */
    @SerializedName("label")
    public String label;

    /**
     * Indique si l'item est fait ou non (vaut 1 si fait, vaut n'importe quel autre entier sinon)
     */
    @SerializedName("checked")
    public int checked;

}
