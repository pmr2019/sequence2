package com.example.todo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.todo.models.ListeToDo;

import java.util.List;

@Dao
public interface ListeToDoDao {

    @Query("SELECT * FROM ListeToDo WHERE id = :id")
    ListeToDo getListeToDo(int id);

    @Query("SELECT * FROM ListeToDo WHERE ListeToDo.profilListeToDoId = :profileListeToDoId")
    List<ListeToDo> getAllListeToDo(String profileListeToDoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addListeToDo(ListeToDo listeToDo);

    @Query("DELETE FROM ListeToDo WHERE ListeToDo.profilListeToDoId = :profilListeToDoId")
    void delAllListeToDo(String profilListeToDoId);
}
