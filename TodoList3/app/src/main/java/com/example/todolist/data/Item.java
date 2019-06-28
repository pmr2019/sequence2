package com.example.todolist.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    private long mId;

    @SerializedName("label")
    private String mDescription;

    @SerializedName("checked")
    private int mDone;

    private static int PRIM_ID = 0;

    public Item() {
        mDescription = "";
        mDone = 0;
        mId = 0;
    }

    public Item(String description) {
        mDescription = description;
        mDone = 0;
        mId = PRIM_ID++;
    }

    public Item(String description, int done) {
        mDescription = description;
        mDone = done;
        mId = PRIM_ID++;
    }

    public long getId() {return mId; }
    public String getDescription() {
        return mDescription;
    }
    public int getDone() {
        return mDone;
    }

    public void setId(long id) { mId = id; }
    public void setDescription(String description) { mDescription = description; }
    public void setDone(int done) { mDone = done; }

    @NonNull
    @Override
    public String toString() {
        String msg;
        if (mDone == 1) { msg = " (état : fait)"; }
        else { msg = " (état : non fait)"; }

        return "[item :" + mDescription + msg + "]";
    }
}
