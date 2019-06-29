package PMR.ToDoList.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import PMR.ToDoList.data.Model.ToDoList;

@Dao
public interface ToDoListDao {

    @Insert
    void insert(ToDoList todolist);

    @Query("DELETE FROM toDoList_table")
    void deleteAllToDoLists();

    @Query("SELECT * from toDoList_table ORDER BY idToDoList ASC")
    List<ToDoList> getAllToDoLists();
}
