package com.ecl.maxime.application_todoliste.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Max on 2019-05-19.
 */
public class ListeToDo implements Serializable {

    private String mTitreListe;
    private ArrayList<ItemToDo> mLesItems;

    public ListeToDo(String titreListe) {
        mTitreListe = titreListe;
    }

    public String getTitreListe() {
        return mTitreListe;
    }

    public void setTitreListe(String titreListe) {
        mTitreListe = titreListe;
    }

    public ArrayList<ItemToDo> getLesItems() {
        return mLesItems;
    }

    public void setLesItems(ArrayList<ItemToDo> lesItems) {
        mLesItems = lesItems;
    }

    public ItemToDo rechercherItem(String descriptionItem){
        return mLesItems.get(0);
    }

    public void ajouteItem(ItemToDo itemToDo){
        mLesItems.add(itemToDo);
    }

    @Override
    public String toString() {
        return "ListeToDo{" +
                "mTitreListe='" + mTitreListe + '\'' +
                ", mLesItems=" + mLesItems +
                '}';
    }
}
