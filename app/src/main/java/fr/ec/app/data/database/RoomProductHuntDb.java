package fr.ec.app.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import fr.ec.app.data.database.dao.PostDao;
import fr.ec.app.data.model.Post;

@Database(entities = { Post.class, }, version = 3)
public abstract class RoomProductHuntDb extends RoomDatabase {

  public abstract PostDao postDao();

  //SINGLETON
  private static RoomProductHuntDb INSTANCE;

  public static RoomProductHuntDb getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (RoomProductHuntDb.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomProductHuntDb.class,
              "roomproducthuntdb").allowMainThreadQueries().build();
        }
      }
    }
    return INSTANCE;
  }

}
