package com.example.todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todo.database.dao.ItemDao;
import com.example.todo.database.dao.ListDao;
import com.example.todo.database.dao.RequestDao;
import com.example.todo.model.ItemToDo;
import com.example.todo.model.ListeToDo;
import com.example.todo.model.WaitingRequest;

@Database(entities = { ListeToDo.class, ItemToDo.class, WaitingRequest.class}, version = 5)
public abstract class RoomProductHuntDb extends RoomDatabase {

  public abstract ItemDao itemDao();
  public abstract ListDao listDao();
  public abstract RequestDao requestDao();

  //SINGLETON
  private static RoomProductHuntDb INSTANCE;

  public static RoomProductHuntDb getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (RoomProductHuntDb.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomProductHuntDb.class,
              "roomproducthuntdb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
      }
    }
    return INSTANCE;
  }

}
