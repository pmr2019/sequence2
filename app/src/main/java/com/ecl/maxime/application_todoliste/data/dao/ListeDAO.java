package com.ecl.maxime.application_todoliste.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ecl.maxime.application_todoliste.data.Liste;

import java.util.List;

/**
 * Created by Max on 2019-06-30.
 */

@Dao
public interface ListeDAO {

    @Query("SELECT * FROM lists")
    List<Liste> getLists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveListes(List<Liste> lists);
}
