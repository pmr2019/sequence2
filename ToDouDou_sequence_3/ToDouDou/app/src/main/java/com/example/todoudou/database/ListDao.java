package com.example.todoudou.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListDao {
    @Query("SELECT * FROM listetodo WHERE idProfil = :idUser")
    List<ListeToDo> getAll(int idUser);

    @Query("DELETE FROM listetodo WHERE listetodo.id = :id AND idProfil = :idUser")
    void delete(int id, int idUser);

    @Insert
    void insert(ListeToDo liste);

    @Query("DELETE FROM listetodo WHERE idProfil = :idUser")
    void deleteAllUser(int idUser);
}
