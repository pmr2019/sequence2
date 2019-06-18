package com.example.td_wang_yang_wei.DataClass;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListeToDo implements Serializable {
    private String titreListeToDo;
    private List<ItemToDo> lesItems;

    ListeToDo() {
        lesItems = new ArrayList<>();
    }


    public ListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
        lesItems = new ArrayList<>();
    }

    String getTitreListeToDo() {
        return titreListeToDo;
    }

    public void setTitreListeToDo(String titreListeToDo) {
        this.titreListeToDo = titreListeToDo;
    }

    private List<ItemToDo> getLesItems() {
        return lesItems;
    }

    public void setLesItems(List<ItemToDo> lesItems) {
        this.lesItems = lesItems;
    }

    void ajouterItem(ItemToDo unItem)
    {
        this.lesItems.add(unItem);
    }

    public void validerItem(String item,Boolean isChecked)
    {
        for (ItemToDo i : lesItems) {
            if (i.getDescription().equals(item)){
                i.setFait(isChecked);
            }
        }
    }


    public int rechercherItem(String s)
    {
        int retour = -1;
        Boolean trouve = Boolean.FALSE;
        for (int i=0; i < this.lesItems.size() ;i++)
        {
            if (this.lesItems.get(i).getDescription().equals(s))
            {
                retour=i;
                i=this.lesItems.size();
                trouve=Boolean.TRUE;
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
}

