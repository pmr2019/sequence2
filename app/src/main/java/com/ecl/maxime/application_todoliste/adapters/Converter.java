package com.ecl.maxime.application_todoliste.adapters;

import com.ecl.maxime.application_todoliste.api_request.ItemResponse;
import com.ecl.maxime.application_todoliste.api_request.ListeResponse;
import com.ecl.maxime.application_todoliste.data.Item;
import com.ecl.maxime.application_todoliste.data.Liste;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 2019-06-30.
 */
public class Converter {

    public static Item from(ItemResponse itemResponse) {
        Item item = new Item();
        item.setId(itemResponse.getId());
        item.setLabel(itemResponse.getLabel());
        item.setChecked(itemResponse.isChecked());
        return item;
    }

    public static Liste from(ListeResponse listeResponse){
        Liste liste = new Liste();
        liste.setId(listeResponse.getId());
        liste.setLabel(listeResponse.getLabel());
        return liste;
    }

    public static List<Liste> fromListe(List<ListeResponse> listeResponseList){
        List<Liste> lists = new ArrayList<>(0);
        for (ListeResponse listeResponse : listeResponseList){
            lists.add(from(listeResponse));
        }
        return lists;
    }

    public static List<Item> fromItem(List<ItemResponse> itemResponseList){
        List<Item> items = new ArrayList<>(0);
        for (ItemResponse itemResponse : itemResponseList){
            items.add(from(itemResponse));
        }
        return items;
    }
}
