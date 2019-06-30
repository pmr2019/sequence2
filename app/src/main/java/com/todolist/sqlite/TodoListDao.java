package com.todolist.sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.todolist.sqlite.data.Item;
import com.todolist.sqlite.data.List;
import com.todolist.sqlite.data.User;

@Dao
public interface TodoListDao {

    // Part List
    //----------------------------------------------------------

    @Query("SELECT * FROM list")
    java.util.List<List> getLists();

    @Query("SELECT * FROM list WHERE user_id LIKE:userId")
    java.util.List<List> findListByUserId(String userId);

    @Query("SELECT * FROM list WHERE label LIKE:labelClick")
    List findListByUserLabel(String labelClick);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveList(java.util.List<List> listsLoad);

    @Insert
    void add(List listAdd);

    @Delete
    int deleteList(List listdb);

    @Query("DELETE FROM list")
    int deleteAllList();

    // Part Item
    //----------------------------------------------------------

    @Query("SELECT * FROM item")
    java.util.List<Item> getAllItem();

    @Query("SELECT * FROM item WHERE list_id LIKE:listId")
    java.util.List<Item> findItemBylistId(String listId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveItem(java.util.List<Item> item);

    @Update
    void updateItem(Item item);

    @Query("DELETE FROM item")
    int deleteAllItem();

    @Delete
    void deleteItem(Item item);

    // Part User
    //----------------------------------------------------------

    @Query("SELECT * FROM users")
    java.util.List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE id IN(:userIds)")
    java.util.List<User> loadAllUsersByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE pseudo LIKE:pseudo")
    java.util.List<User> findUserByPseudo(String pseudo);

    @Insert
    void setUser(User user);
}
