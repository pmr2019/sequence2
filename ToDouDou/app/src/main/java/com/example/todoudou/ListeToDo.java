package com.example.todoudou;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListeToDo implements Serializable {
    private String titreListeToDo;
    private List<ItemToDo> lesItems;
    private int id = 0;

    public ListeToDo() {
        lesItems = new ArrayList<ItemToDo>();
    }

    public ListeToDo(String titreListeToDo, List<ItemToDo> lesItems) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
        this.lesItems = lesItems;
    }

    public ListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
    }

    public ListeToDo(String titreListeToDo, int id) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<ItemToDo>();
        this.id = id;
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

}