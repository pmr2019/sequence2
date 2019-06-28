package com.ecl.maxime.application_todoliste.classes;

import java.io.Serializable;

/**
 * Created by Max on 2019-05-19.
 */
public class ItemToDo implements Serializable {

    private String mDescription;
    private boolean mFait = false;

    public ItemToDo(String description, boolean fait) {
        mDescription = description;
        mFait = fait;
    }

    public ItemToDo(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isFait() {
        return mFait;
    }

    public void setFait(boolean fait) {
        mFait = fait;
    }

    @Override
    public String toString() {
        return "ItemToDo{" +
                "mDescription='" + mDescription + '\'' +
                ", mFait=" + mFait +
                '}';
    }
}
