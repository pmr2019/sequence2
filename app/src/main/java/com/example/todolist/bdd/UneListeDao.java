package com.example.todolist.bdd;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todolist.api.response_class.UneListe;

import java.util.List;

@Dao
public interface UneListeDao {

    /**
     * Permet de récupérer l'ensemble des ToDoLists de l'utilisateur courant stockées dans la BDD
     *
     * @param hash le hahs de l'utilisateur courant
     * @return les ToDolists de l'utilisateur
     */
    @Query("SELECT * FROM lists WHERE hash = :hash ")
    List<UneListe> getAll(String hash);

    /**
     * Permet d'ajouter une liste de ToDoLists à la BDD
     *
     * @param todolists la liste d ToDoLists à ajouter
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UneListe> todolists);
}
