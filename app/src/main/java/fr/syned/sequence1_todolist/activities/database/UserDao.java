package fr.syned.sequence1_todolist.activities.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    User findByName(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void replaceAll(User... users);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
