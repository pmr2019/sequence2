package com.ecl.maxime.application_todoliste.repos;

import androidx.lifecycle.LiveData;

import com.ecl.maxime.application_todoliste.api_request.Item;
import com.ecl.maxime.application_todoliste.classes.ItemToDo;
import com.ecl.maxime.application_todoliste.database.dao.Itemsdao;

import java.util.ArrayList;

public class ItemsDataRepo {

        private final Itemsdao itemsdao;

        public ItemsDataRepo(Itemsdao itemsdao) { this.itemsdao = itemsdao; }

        // --- GET ITEMS ---
        public ArrayList<Item> getItems(String titreListe) { return this.itemsdao.getItems(titreListe); }

        // --- CREATE ---

        public void createItem(Item item){ itemsdao.insertItem(item); }

        // --- UPDATE ---

        public void updateItem(Item item){ itemsdao.updateItem(item); }
}
