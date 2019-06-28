package com.example.todoudou.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProfilDao {

    @Query("SELECT uid FROM profillistetodo WHERE pseudo = :pseudo AND pass = :pass")
    int getId(String pseudo, String pass);

    @Insert
    void insert(ProfilListeToDo profil);


}
