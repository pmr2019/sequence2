package com.example.todolist.api.response_class;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Définition de la classe User.
 * Cette classe permet de récupérer la réponse de l'API lors de la requête de demande
 * de connexion
 */
@Entity(tableName = "users")
public class User {

    /**
     * Le hash d'identification récupéré
     */
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "hash")
    @SerializedName("hash")
    public String hash;

    @ColumnInfo(name = "pseudo")
    @SerializedName("pseudo")
    public String pseudo;


    @ColumnInfo(name = "id")
    @SerializedName("id")
    public String id;

    @ColumnInfo(name = "pass")
    @SerializedName("pass")
    public String pass;

}
