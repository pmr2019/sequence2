package com.example.todolist.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolist.ItemToDo;
import com.example.todolist.ListeToDo;
import com.example.todolist.ProfilListeToDo;
import com.example.todolist.database.dao.ItemDao;
import com.example.todolist.database.dao.ListDao;
import com.example.todolist.database.dao.UserDao;

@Database(entities = {ProfilListeToDo.class, ListeToDo.class, ItemToDo.class}, version = 1, exportSchema = false)
public abstract class TodoListDataBase extends RoomDatabase {
    // --- SINGLETON ---
    private static volatile TodoListDataBase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();
    public abstract ListDao listDao();
    public abstract ItemDao itemDao();

    // --- INSTANCE ---
    public static TodoListDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TodoListDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoListDataBase.class, "MyDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
