package com.example.todolist.bdd;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.api.response_class.UnItem;

import java.util.List;

@Dao
public interface UnItemDao {
    /**
     * Permet de récupérer l'ensemble des items associés à une ToDoList auprès de la BDD
     *
     * @param idListe l'identifiant de la ToDoList concernée
     * @return la liste des items de la ToDoList
     */
    @Query("SELECT * FROM items WHERE idListe LIKE :idListe")
    List<UnItem> getAll(int idListe);

    /**
     * Permet de mettre à jour un item auprès de la BDD
     *
     * @param item l'item à mettre à jour
     */
    @Update
    void updateItem(UnItem item);

    /**
     * Permet d'ajouter une liste d'items à la BDD
     *
     * @param items la liste des items à ajouter
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UnItem> items);
}
