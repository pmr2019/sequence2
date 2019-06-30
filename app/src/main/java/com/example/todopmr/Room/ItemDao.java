package com.example.todopmr.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todopmr.Modele.ItemToDo;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createItem(ItemToDo item);

    @Query("SELECT * FROM ItemToDo")
    List<ItemToDo> getAll();

    @Query("SELECT * FROM ItemToDo WHERE idItem = :itemId")
    ItemToDo findbyId(int itemId);

    @Query("SELECT * FROM ItemToDo WHERE label = :first LIMIT 1")
    ItemToDo findByName(String first);

    @Query("SELECT * FROM ItemToDo WHERE listeId = :listeId")
    List<ItemToDo> findbyListId(int listeId);

    @Insert
    long insertItem(ItemToDo item);

    @Update
    int updateItem(ItemToDo item);

    @Delete
    void deleteItem(ItemToDo item);

    @Query("DELETE FROM ItemToDo")
    void clean();
}
