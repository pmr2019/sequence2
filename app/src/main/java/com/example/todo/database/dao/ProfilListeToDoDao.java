package com.example.todo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.todo.models.ListeToDo;
import com.example.todo.models.ProfilListeToDo;

@Dao
public interface ProfilListeToDoDao {

    @Query("SELECT * FROM ProfilListeToDo WHERE id = :id")
    ProfilListeToDo getProfilListeToDo(int id);

    @Query("SELECT id FROM ProfilListeToDo WHERE login = :login")
    Integer getIdProfilListeToDo(String login);

    @Query("SELECT * FROM ProfilListeToDo WHERE login = :login")
    ProfilListeToDo getProfilListeToDoByLogin(String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProfilListeToDo(ProfilListeToDo profilListeToDo);
}