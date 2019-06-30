package com.example.todopmr.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todopmr.Modele.ListeToDo;

import java.util.List;

@Dao
public interface ListeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createListe(ListeToDo liste);

    @Query("SELECT * FROM ListeToDo")
    List<ListeToDo> getAll();

    @Query("SELECT * FROM ListeToDo WHERE idListe = :listId")
    ListeToDo findbyId(int listId);

    @Query("SELECT * FROM ListeToDo WHERE label = :first LIMIT 1")
    ListeToDo findByName(String first);

    @Query("SELECT * FROM ListeToDo WHERE profilId = :profilId")
    List<ListeToDo> findbyPseudoId(int profilId);

    @Insert
    long insertList(ListeToDo list);

    @Update
    int updateList(ListeToDo list);

    @Delete
    void deleteList(ListeToDo list);

    @Query("DELETE FROM ListeToDo")
    void clean();
}

