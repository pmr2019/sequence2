package com.example.td_wang_yang_wei.DataClass;

import java.io.Serializable;

public class Utilisateur implements Serializable {
    private String pseudo;
    private String motDePasse;
    private String hash;

    public Utilisateur(String pseudo, String motDePasse) {

        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.hash = "";
    }
    public Utilisateur(String pseudo, String motDePasse,String hash) {

        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.hash = hash;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public Boolean verifierMotDePasse(String motDePasse){
        return this.motDePasse.equals(motDePasse);
    }

}
