package com.example.todolist.bdd;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todolist.api.response_class.User;

@Dao
public interface UserDao {

    @Query("SELECT hash FROM users WHERE pseudo LIKE :pseudo AND " +
            "pass LIKE :pass LIMIT 1")
    String findHash(String pseudo, String pass);

    @Insert
    void insertAll(User... users);

}
