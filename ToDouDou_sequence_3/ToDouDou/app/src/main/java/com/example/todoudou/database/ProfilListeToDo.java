package com.example.todoudou.database;


import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.io.Serializable;
import java.util.List;

@Entity
public class ProfilListeToDo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "pseudo")
    private String login;

    @ColumnInfo(name = "pass")
    private String pass;

    @Ignore
    private List<ListeToDo> mesListeToDo = null;


    public ProfilListeToDo(String login, String pass){
        this.login = login;
        this.pass = pass;
    }

    public List<ListeToDo> getMesListeToDo() {
        return mesListeToDo;
    }

    public void setMesListeToDo(List<ListeToDo> mesListeToDo) {
        this.mesListeToDo = mesListeToDo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void ajouteListe(ListeToDo uneListe)
    {
        this.mesListeToDo.add(uneListe);
    }
    public int rechercherList(String s)
    {
        int retour = -1;
        for (int i=0; i < this.mesListeToDo.size() ;i++)
        {
            Log.i("fusion", "list " + i + " : " + this.mesListeToDo.get(i).toString());
            if (this.mesListeToDo.get(i).getTitreListeToDo().equals(s))
            {
                Log.i("fusion", "i " + i);
                retour=i;
                i=this.mesListeToDo.size();
            }
        }
        Log.i("fusion", "retour " + retour);
        return retour;
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + mesListeToDo + ", login=" + login + '}';
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}