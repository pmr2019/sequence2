package com.example.todopmr.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todopmr.Modele.ItemToDo;
import com.example.todopmr.Modele.ListeToDo;
import com.example.todopmr.Modele.ProfilListeToDo;


@Database(entities = {ProfilListeToDo.class, ListeToDo.class, ItemToDo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ListeDao listeDao();
    public abstract ItemDao itemDao();

    private static AppDatabase database;

    public static AppDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            "database-todo").fallbackToDestructiveMigration().build();
                }
            }
        }
        return database;
    }
}
