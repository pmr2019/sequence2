package PMR.ToDoList.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import PMR.ToDoList.data.Model.Task;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Delete
    void deleteAllToDOListTasks(ArrayList<Task> ToDoListTasks);

    @Query("SELECT * from task_table INNER JOIN toDoList_table ON (keyToDoList=idToDoList)WHERE keyToDoList=:toDoListId ORDER BY idTask ASC")
    List<Task> getAllToDoListTasks(int toDoListId);

}
