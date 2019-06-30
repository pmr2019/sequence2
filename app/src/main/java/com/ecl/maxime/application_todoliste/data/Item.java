package com.ecl.maxime.application_todoliste.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Max on 2019-06-30.
 */
@Entity(tableName = "items")
public class Item {

    @PrimaryKey
    private long id;

    private String label;

    private boolean checked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
