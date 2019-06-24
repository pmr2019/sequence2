package com.example.todolist.database.dao;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.ItemToDo;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM ItemToDo WHERE idListAssocie = :idList")
    List<ItemToDo> getItem(int idList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(ItemToDo item);

    @Query("UPDATE ItemToDo SET fait = :fait, modifie='true' WHERE idItem= :idItem")
    int updateItem(boolean fait, int idItem);

    @Query("SELECT fait FROM ItemToDo WHERE idItem = :idItem")
    boolean getFaitBdd(int idItem);

    @Query("SELECT * FROM ItemToDo WHERE modifie='true'")
    List<ItemToDo> getItemModifie();
}
