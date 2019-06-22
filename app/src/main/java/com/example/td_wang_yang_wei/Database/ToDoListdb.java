package com.example.td_wang_yang_wei.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.td_wang_yang_wei.Database.dao.ItemDao;
import com.example.td_wang_yang_wei.Database.dao.ListDao;
import com.example.td_wang_yang_wei.Database.dao.UserDao;
import com.example.td_wang_yang_wei.Database.model.Itemdb;
import com.example.td_wang_yang_wei.Database.model.Listdb;
import com.example.td_wang_yang_wei.Database.model.Userdb;

@Database(entities = {Userdb.class, Listdb.class, Itemdb.class}, version = 1)
public abstract class ToDoListdb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ListDao listDao();
    public abstract ItemDao itemDao();

    private static ToDoListdb db;

    public static ToDoListdb getDatabase(final Context context){
        if (db == null) {
            synchronized(ToDoListdb.class){
                if(db == null){
                    db = Room.databaseBuilder(context.getApplicationContext(),ToDoListdb.class,
                            "todolistdatabase").allowMainThreadQueries().build();
                }
            }
        }
        return db;

    }
}
