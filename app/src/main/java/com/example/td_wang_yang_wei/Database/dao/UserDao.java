package com.example.td_wang_yang_wei.Database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.td_wang_yang_wei.Database.model.Userdb;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<Userdb> getAllUsers();

    @Query("SELECT * FROM users WHERE id IN(:userIds)")
    List<Userdb> loadAllUsersByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE pseudo LIKE:pseudo")
    List<Userdb> findUserByPseudo(String pseudo);

    @Insert
    void postUsers(Userdb users);

    //TODO:根据需求补全


}
