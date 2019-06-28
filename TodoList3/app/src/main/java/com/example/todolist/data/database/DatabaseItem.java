package com.example.todolist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseItem {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "idList")
    public long idList;

    @ColumnInfo(name = "label")
    public String description;

    @ColumnInfo(name = "checked")
    public int done;
}
