package com.example.todolist.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todolist.ListeToDo;

import java.util.List;

@Dao
public interface ListDao {

    @Query("SELECT * FROM ListeToDo WHERE idUserAssocie = :idUser")
    List<ListeToDo>getLists(int idUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertList(ListeToDo list);
}
