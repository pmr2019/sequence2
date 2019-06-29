package PMR.ToDoList.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import PMR.ToDoList.data.Model.Task;
import PMR.ToDoList.data.Model.ToDoList;
import PMR.ToDoList.data.Model.User;
import PMR.ToDoList.data.database.dao.TaskDao;
import PMR.ToDoList.data.database.dao.ToDoListDao;
import PMR.ToDoList.data.database.dao.UserDao;

@androidx.room.Database(entities = {User.class, ToDoList.class, Task.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ToDoListDao toDoListDao();
    public abstract TaskDao taskDao();

    private static volatile Database INSTANCE;

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "roomDatabase").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

}