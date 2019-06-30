package com.ecl.maxime.application_todoliste.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ecl.maxime.application_todoliste.data.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 2019-06-30.
 */

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM items")
    List<Item> getItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveItems(List<Item> items);
}
