package com.example.td_wang_yang_wei;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListeDeUtilisateur implements Serializable {

    private final String urlDefault="http://tomnab.fr/todo-api/";
    private String url;
    private List<Utilisateur> utilisateurs;

    public ListeDeUtilisateur() {
        this.url = urlDefault;
        this.utilisateurs = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void AjouterUtilisateur(Utilisateur utilisateur){
        this.utilisateurs.add(utilisateur);
    }

    public Utilisateur ChercheUtilisateur(String pseudo){
        if(this.VerifierPresence(pseudo)){
            for(int i=0;i<this.utilisateurs.size();i++){
                if(this.utilisateurs.get(i).getPseudo().equals(pseudo))return this.utilisateurs.get(i);
            }
        }
        return null;
    }
    public Boolean VerifierPresence(String pseudo){
        if(this.utilisateurs.isEmpty())return false;
        for(int i=0;i<this.utilisateurs.size();i++){
            if(this.utilisateurs.get(i).getPseudo().equals(pseudo))return true;
        }
        return false;
    }
    public Utilisateur getLastUser() {
        if (utilisateurs.isEmpty()) {
            return null;
        }
        return this.utilisateurs.get(utilisateurs.size() - 1);
    }
}
