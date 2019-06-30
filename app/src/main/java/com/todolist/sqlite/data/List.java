package com.todolist.sqlite.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "list")
public class List {
    @PrimaryKey
    @NonNull
    public String id;
    public String label;
    @ColumnInfo(name = "user_id")
    public String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUserId(){ return userId;}

    public void setUserId(String userId){this.userId = userId;}
}
