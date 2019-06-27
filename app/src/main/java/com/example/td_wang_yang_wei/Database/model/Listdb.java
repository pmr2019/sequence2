package com.example.td_wang_yang_wei.Database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "lists")
//        indices = {@Index(value = {"id"})},
//        foreignKeys = @ForeignKey(entity = Userdb.class,
//                parentColumns = "id",
//                childColumns = "user_id",
//                onDelete = CASCADE))
public class Listdb {
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
