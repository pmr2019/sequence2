package PMR.ToDoList.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import PMR.ToDoList.data.Model.Task;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllUsers();

    @Query("SELECT * from task_table ORDER BY idTask ASC")
    LiveData<List<Task>> getAllTasks();

}
