package com.example.todo.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todo.model.WaitingRequest;

import java.util.List;


@Dao
public interface RequestDao {
  @Query("SELECT * FROM waitingRequests") List<WaitingRequest> getRequests();


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(WaitingRequest request);

  @Query("DELETE FROM waitingRequests WHERE url=:url")
  void delete(String url);


}
