package com.example.todo.database.dao;

import android.content.ClipData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todo.model.ItemToDo;
import com.example.todo.model.ListeToDo;

import java.util.List;


@Dao
public interface ItemDao {
  @Query("SELECT * FROM items WHERE idListe=:idListe") List<ItemToDo> getItems(Integer idListe);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void save(List<ItemToDo> items);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(ItemToDo item);

  @Query("DELETE FROM items WHERE id=:id")
  void delete(Integer id);

  @Query("SELECT * FROM items WHERE id=:id") ItemToDo getItem(Integer id);

}
