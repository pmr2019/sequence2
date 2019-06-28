package com.ecl.maxime.application_todoliste.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecl.maxime.application_todoliste.api_request.Item;
import com.ecl.maxime.application_todoliste.api_request.Liste;
import com.ecl.maxime.application_todoliste.classes.ListeToDo;
import com.ecl.maxime.application_todoliste.classes.ProfileListeToDo;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface Listesdao {

    @Query("SELECT mMesListeToDo FROM ProfileListeToDo WHERE login = :userId AND mdp = :password")
    ArrayList<Liste> getListes(String userId, String password);

    @Insert
    String insertItem(Liste liste);

}
