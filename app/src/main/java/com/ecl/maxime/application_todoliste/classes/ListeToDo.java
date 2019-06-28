package com.ecl.maxime.application_todoliste.classes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Max on 2019-05-19.
 */

@Entity
public class ListeToDo implements Serializable {

    @NonNull
    @PrimaryKey
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
