package com.ecl.maxime.application_todoliste.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Max on 2019-06-30.
 */

@Entity(tableName = "lists")
public class Liste {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "label")
    private String label;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
