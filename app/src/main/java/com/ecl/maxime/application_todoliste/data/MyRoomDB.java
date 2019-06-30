package com.ecl.maxime.application_todoliste.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ecl.maxime.application_todoliste.data.dao.ItemDAO;
import com.ecl.maxime.application_todoliste.data.dao.ListeDAO;

/**
 * Created by Max on 2019-06-30.
 */

@Database(entities = { Liste.class, Item.class}, version = 3, exportSchema = false)
public abstract class MyRoomDB extends RoomDatabase {

    public abstract ListeDAO listeDAO();
    public abstract ItemDAO itemDAO();

    private static MyRoomDB INSTANCE;

    public static MyRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyRoomDB.class,
                            "roomdb").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
