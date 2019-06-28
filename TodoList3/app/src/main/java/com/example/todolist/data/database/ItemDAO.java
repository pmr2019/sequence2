package com.example.todolist.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM databaseitem WHERE idList = :idList")
    List<DatabaseItem> getItems(long idList);

    @Query("UPDATE databaseitem SET checked = :checked WHERE id = :idItem")
    void updateStatus(int checked, long idItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(List<DatabaseItem> itemList);
}
