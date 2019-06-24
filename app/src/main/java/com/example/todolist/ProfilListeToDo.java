package com.example.todolist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class ProfilListeToDo {
    @PrimaryKey
    public int idUser;
    public String login;
    private String passe;

    @Ignore
    public ProfilListeToDo(){
    }
    @Ignore
    public ProfilListeToDo(String login,String passe) {
        this.login = login;
        this.passe=passe;
    }


    public ProfilListeToDo(String login,String passe, int idUser) {
        this.login = login;
        this.idUser = idUser;
        this.passe=passe;
    }


    public String getLogin() {
        return login;
    }
    public int getIdUser() {return this.idUser;}
    public String getPasse() { return passe;}

    public void setLogin(String login) {
        this.login = login;
    }
    public void setPasse(String passe) {this.passe = passe; }
    public void setIdUser(int idUser){this.idUser=idUser;}

    @Override
    public String toString() {
        return "ProfilListeToDo{" +
                "login='" + login + '\'' +
                " mot de passe='" + passe + '\'' +
                '}';
    }
}
