package com.todolist.sqlite;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.todolist.sqlite.data.Item;
import com.todolist.sqlite.data.List;
import com.todolist.sqlite.data.User;

@Database(entities = {User.class, List.class, Item.class}, version = 1)
public abstract class TodoListDB extends RoomDatabase{
    public abstract TodoListDao todoListDao();

    private static TodoListDB db;

    public static TodoListDB getDatabase(final Context context){
        if (db == null) {
            synchronized(TodoListDB.class){
                if(db == null){
                    db = Room.databaseBuilder(context.getApplicationContext(),TodoListDB.class,
                            "todolistdatabase.db").build();
                }
            }
        }
        return db;
    }
}
