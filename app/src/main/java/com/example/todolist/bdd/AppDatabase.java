package com.example.todolist.bdd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todolist.api.response_class.UnItem;
import com.example.todolist.api.response_class.UneListe;
import com.example.todolist.api.response_class.User;

/**
 * Définition de la classe AppDatabase.
 * Cette classe représente la base de données de l'application
 */
@Database(version = 3, entities = {User.class, UneListe.class, UnItem.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static String DB_name = "database";

    abstract public UneListeDao listeDao();

    abstract public UserDao userDao();

    abstract public UnItemDao itemDao();


    /**
     * Permet de générer une unique instance de la BDD
     *
     * @param context le context de l'application
     * @return une l'instance de la BDD
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_name)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
