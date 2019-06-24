package com.example.todolist.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todolist.ProfilListeToDo;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM ProfilListeToDo")
    List<ProfilListeToDo> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(ProfilListeToDo user);
}

