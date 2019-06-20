package com.example.todolist.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoList implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("label")
    private String mTitle;

    private List<Item> mItems;

    private static int PRIM_ID = 0;

    public TodoList() {
        mTitle = "";
        mItems = new ArrayList<>();
        mId = PRIM_ID++;
    }

    public int getId() { return mId; }
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    @Override
    public String toString() {
        String msg = "[liste :" + mTitle;
        if(mItems.isEmpty()) {
            msg += " (etat: vide)";
        } else {
            msg += " (contient";
            for (Item item : mItems) {
                msg += " " + item.toString();
            }
            msg += ")";
        }
        msg += "]";

        return msg;
    }
}
