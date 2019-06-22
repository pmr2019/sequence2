package com.example.td_wang_yang_wei.Database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"id"})},
        tableName = "users")
public class Userdb {

    @PrimaryKey
    @NonNull
    public int id;

    public String pseudo;

    public String motDePasse;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPseudo(){
        return pseudo;
    }

    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    public String getMotDePasse(){
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse){
        this.motDePasse = motDePasse;
    }



}