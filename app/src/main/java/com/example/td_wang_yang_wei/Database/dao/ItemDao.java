package com.example.td_wang_yang_wei.Database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.td_wang_yang_wei.Database.model.Itemdb;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM items")
    List<Itemdb> getAllLists();

    @Query("SELECT * FROM items WHERE list_id LIKE:listId")
    List<Itemdb> findItemBylistId(String listId);

    @Insert
    void saveItem(Itemdb itemdb);

    @Update
    void updateItem(Itemdb itemdb);

    @Delete
    void deleteItem(Itemdb itemdb);

    //TODO:根据需求补全
}
