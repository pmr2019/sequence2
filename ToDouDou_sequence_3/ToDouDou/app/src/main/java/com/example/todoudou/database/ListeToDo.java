package com.example.todoudou.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ListeToDo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid = 0;

    @ForeignKey(entity = ProfilListeToDo.class,parentColumns = "uid",childColumns = "idProfil")
    private int idProfil;

    @ColumnInfo(name = "id")
    private int id = 0;

    @ColumnInfo(name = "titre")
    private String titreListeToDo;

    @Ignore
    private List<ItemToDo> lesItems;

    @Ignore
    public ListeToDo() {
        lesItems = new ArrayList<ItemToDo>();
    }

    @Ignore
    public ListeToDo(String titreListeToDo, List<ItemToDo> lesItems) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
        this.lesItems = lesItems;
    }

    @Ignore
    public ListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
    }

    @Ignore
    public ListeToDo(String titreListeToDo, int id) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
        this.id = id;
    }

    public ListeToDo(String titreListeToDo, int id, int idProfil) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
        this.id = id;
        this.idProfil = idProfil;
    }

    public String getTitreListeToDo() {
        return titreListeToDo;
    }

    public void setTitreListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
    }

    public List<ItemToDo> getLesItems() {
        return lesItems;
    }

    public void setLesItems(List<ItemToDo> lesItems) {
        this.lesItems = lesItems;
    }

    public void ajouterItem(ItemToDo unItem)
    {
        this.lesItems.add(unItem);
    }

    public Boolean validerItem(String s, boolean valider)
    {
        int indice = 1;

        if ((indice = rechercherItem(s)) >=0)
        {
            this.lesItems.get(indice).setFait(valider);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    public int rechercherItem(String s)
    {
        int retour = -1;
        for (int i=0; i < this.lesItems.size() ;i++)
        {
            if (this.lesItems.get(i).getDescription().equals(s))
            {
                retour=i;
                i=this.lesItems.size();
            }
        }
        return retour;
    }
    @Override
    public String toString() {
        String retour;
        retour = "Liste : " + this.getTitreListeToDo()+ "Items : " + this.getLesItems().toString();
        return retour;
    }


    public int getId(){
        return this.id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProfil() {
        return idProfil;
    }

    public void setIdProfil(int idProfil) {
        this.idProfil = idProfil;
    }


//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this)
//            return true;
//        if (getClass() != obj.getClass())
//            return false;
//        ListeToDo pn = (ListeToDo) obj;
//        return pn.id == id && pn.lesItems.equals(lesItems);
//    }

}