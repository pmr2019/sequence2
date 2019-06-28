package com.ecl.maxime.application_todoliste.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ecl.maxime.application_todoliste.classes.ItemToDo;
import com.ecl.maxime.application_todoliste.classes.ListeToDo;
import com.ecl.maxime.application_todoliste.classes.ProfileListeToDo;
import com.ecl.maxime.application_todoliste.database.dao.Itemsdao;
import com.ecl.maxime.application_todoliste.database.dao.Listesdao;

@Database(entities = {ListeToDo.class, ProfileListeToDo.class}, version = 1, exportSchema = false)
public abstract class SaveMyTripDataBase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile SaveMyTripDataBase INSTANCE;

    // --- DAO ---
    public abstract Itemsdao itemDao();
    public abstract Listesdao listeDao();

    // --- INSTANCE ---
    public static SaveMyTripDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveMyTripDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SaveMyTripDataBase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1);
                contentValues.put("username", "Philippe");
                contentValues.put("urlPicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2");

                db.insert("User", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }
}