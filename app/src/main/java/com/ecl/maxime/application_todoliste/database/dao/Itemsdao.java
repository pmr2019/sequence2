package com.ecl.maxime.application_todoliste.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ecl.maxime.application_todoliste.api_request.Item;
import com.ecl.maxime.application_todoliste.classes.ItemToDo;
import com.ecl.maxime.application_todoliste.classes.ListeToDo;

import java.util.ArrayList;


@Dao
public interface Itemsdao {
    @Query("SELECT mLesItems FROM ListeToDo WHERE mTitreListe = :titreListe")
    ArrayList<Item> getItems(String titreListe);

    @Insert
    String insertItem(Item item);

    @Update
    int updateItem(Item item);
}
