package com.example.todolist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseProfile {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "password")
    public String mPassword;

    @ColumnInfo(name = "userName")
    public String mUserName;
}
