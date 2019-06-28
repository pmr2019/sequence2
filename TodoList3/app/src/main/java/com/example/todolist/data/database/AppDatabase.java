package com.example.todolist.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DatabaseProfile.class, DatabaseItem.class, DatabaseTodoList.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserProfileDAO UserProfileDAO();
    public abstract ItemDAO ItemDAO();
    public abstract TodoListDAO TodoListDAO();
}
