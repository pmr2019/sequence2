package com.example.td_wang_yang_wei.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

public class User {

    @PrimaryKey
    public int id;

    public String pseudo;

    public String motDePasse;

}

@Entity(foreignKeys = @ForeignKey(entity = User.class,
                                  parentColumns = "id",
                                  childColumns = "user_id"))
class List {

    @PrimaryKey
    public String id;

    public String label;

    @ColumnInfo(name = "user_id")
    public String userId;

    }


@Entity(foreignKeys = @ForeignKey(entity = List.class,
        parentColumns = "id",
        childColumns = "list_id"))
class Item {
    @PrimaryKey
    public  String id;

    public String label;

    public Object url;

    public String checked;

    @ColumnInfo(name = "list_id")
    public String listId;
    }
