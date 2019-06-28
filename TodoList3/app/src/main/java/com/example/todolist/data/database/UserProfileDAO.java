package com.example.todolist.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface UserProfileDAO {

    @Query("SELECT * FROM databaseprofile WHERE userName = :userName AND password = :password")
    DatabaseProfile getUser(String userName, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(DatabaseProfile userProfile);
}
