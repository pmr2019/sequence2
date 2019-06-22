package com.example.todo.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.todo.database.dao.ItemToDoDao;
import com.example.todo.database.dao.ListeToDoDao;
import com.example.todo.database.dao.ProfilListeToDoDao;
import com.example.todo.models.ItemToDo;
import com.example.todo.models.ListeToDo;
import com.example.todo.models.ProfilListeToDo;

@android.arch.persistence.room.Database(entities = {ProfilListeToDo.class, ListeToDo.class, ItemToDo.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile MyDatabase INSTANCE;

    // --- DAO ---
    public abstract ProfilListeToDoDao profilListeToDoDao();
    public abstract ListeToDoDao listeToDoDao();
    public abstract ItemToDoDao itemToDoDao();

    // --- INSTANCE ---
    public static MyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "MyDatabase.db")
//                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    // ---
//
//    private static Callback prepopulateDatabase(){
//        return new Callback() {
//
//            @Override
//            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                super.onCreate(db);
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("id", 1);
//                contentValues.put("username", "Philippe");
//                contentValues.put("urlPicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2");
//
//                db.insert("User", OnConflictStrategy.IGNORE, contentValues);
//            }
//        };
//    }
}
