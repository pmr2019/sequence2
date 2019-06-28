package com.example.todoudou.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM itemtodo WHERE itemtodo.idliste = :id")
    List<ItemToDo> getAll(int id);

    @Query("SELECT * FROM itemtodo WHERE itemtodo.idliste = :id")
    List<ItemToDo> get(int id);

    @Query("UPDATE itemtodo SET fait = :fait WHERE itemtodo.uid = :uid")
    void update(int uid, boolean fait);

    @Query("DELETE FROM itemtodo WHERE itemtodo.uid = :uid")
    void delete(int uid);

    @Insert
    void insert(ItemToDo item);

    @Query("SELECT uid FROM itemtodo ORDER BY uid DESC LIMIT 1 ")
    int postInsert();

    @Query("DELETE FROM itemtodo")
    void deleteAll();


    @Query("UPDATE itemtodo SET modifie = :modif WHERE itemtodo.uid = :uid")
    void updateModif(int uid, boolean modif);


}
