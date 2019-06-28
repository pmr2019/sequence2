package com.example.todolist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseTodoList {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "idUser")
    public long idUser;

    @ColumnInfo(name = "label")
    public String title;
}
