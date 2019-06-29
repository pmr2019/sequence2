package PMR.ToDoList.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import PMR.ToDoList.Model.Task;
import PMR.ToDoList.Model.ToDoList;
import PMR.ToDoList.Model.User;

@androidx.room.Database(entities = {User.class, ToDoList.class, Task.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ToDoListDao toDoListDao();
    public abstract TaskDao taskDao();

    private static volatile Database INSTANCE;

    static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "roomDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}