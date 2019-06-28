package com.example.todolist.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodoLists(List<DatabaseTodoList> todoLists);

    @Query("SELECT * FROM databasetodolist WHERE idUser = :idUser")
    List<DatabaseTodoList> getTodoLists(long idUser);
}
