package com.example.todo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.todo.models.ListeToDo;

import java.util.List;

@Dao
public interface ListeToDoDao {

    @Query("SELECT * FROM ListeToDo WHERE id = :id AND ListeToDo.profilListeToDoId = :profileListeToDoId")
    ListeToDo getListeToDo(String profileListeToDoId, int id);

    @Query("SELECT * FROM ListeToDo WHERE ListeToDo.profilListeToDoId = :profileListeToDoId")
    List<ListeToDo> getAllListeToDo(String profileListeToDoId);

    @Query("SELECT COUNT(profilListeToDoId) FROM ListeToDo WHERE profilListeToDoId = :login")
    int countAllListeToDo(String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addListeToDo(ListeToDo listeToDo);

    @Query("DELETE FROM ListeToDo WHERE ListeToDo.profilListeToDoId = :profilListeToDoId")
    void delAllListeToDo(String profilListeToDoId);

    @Delete
    void delListeToDo(ListeToDo listeToDo);

    @Update
    void updateListeToDo(ListeToDo listeToDo);
}
