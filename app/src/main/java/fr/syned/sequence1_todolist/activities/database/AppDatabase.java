package fr.syned.sequence1_todolist.activities.database;

import androidx.room.RoomDatabase;
import androidx.room.Database;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

