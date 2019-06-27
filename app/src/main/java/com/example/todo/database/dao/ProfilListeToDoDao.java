package com.example.todo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.todo.models.ListeToDo;
import com.example.todo.models.ProfilListeToDo;

@Dao
public interface ProfilListeToDoDao {

    @Query("SELECT * FROM ProfilListeToDo WHERE login = :login")
    ProfilListeToDo getProfilListeToDo(String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProfilListeToDo(ProfilListeToDo profilListeToDo);
}