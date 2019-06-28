package com.ecl.maxime.application_todoliste.repos;

import androidx.lifecycle.LiveData;

import com.ecl.maxime.application_todoliste.api_request.Liste;
import com.ecl.maxime.application_todoliste.classes.ItemToDo;
import com.ecl.maxime.application_todoliste.classes.ListeToDo;
import com.ecl.maxime.application_todoliste.database.dao.Itemsdao;
import com.ecl.maxime.application_todoliste.database.dao.Listesdao;

import java.util.ArrayList;

public class ListesDataRepo {

        private final Listesdao listesdao;

        public ListesDataRepo(Listesdao listesdao) { this.listesdao = listesdao; }

        // --- GET LISTES ---
        public ArrayList<Liste> getListes(String login, String mdp) { return this.listesdao.getListes(login,mdp); }

        // --- CREATE ---

        public void createItem(Liste liste){ listesdao.insertItem(liste); }

}
