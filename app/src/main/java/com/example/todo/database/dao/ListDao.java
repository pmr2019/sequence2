package com.example.todo.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todo.model.ListeToDo;

import java.util.List;

@Dao
public interface ListDao {
  @Query("SELECT * FROM lists") List<ListeToDo> getLists();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void save(List<ListeToDo> items);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(ListeToDo list);
}
