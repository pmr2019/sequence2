package com.example.todo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.todo.models.ItemToDo;

import java.util.List;

@Dao
public interface ItemToDoDao {

    @Query("SELECT * FROM ItemToDo WHERE id = :id")
    ItemToDo getItemToDo(int id);

    @Query("SELECT * FROM ItemToDo item WHERE item.listeToDoId = :listeToDoId")
    List<ItemToDo> getAllItemToDo(int listeToDoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItemToDo(ItemToDo itemToDo);

    @Update
    void updateItemToDo(ItemToDo itemToDo);

    @Delete
    void delItemToDo(ItemToDo itemToDo);
}
