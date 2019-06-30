package PMR.ToDoList.data.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.data.Model.ToDoList;

@Dao
public interface ToDoListDao {

    @Insert
    void insert(ToDoList todolist);

    @Query("DELETE FROM toDoList_table")
    void deleteAllToDoLists();


    @Delete
    void deleteAllUserToDoLists(ArrayList<ToDoList> userToDoLists);

    @Query("SELECT * from toDoList_table INNER JOIN user_table ON (keyUser=idUser)WHERE keyUser=:userId ORDER BY idToDoList ASC")
    List<ToDoList> getAllUserToDoLists(int userId);
}
