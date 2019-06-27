package com.example.td_wang_yang_wei;

import com.example.td_wang_yang_wei.Database.model.Itemdb;
import com.example.td_wang_yang_wei.Database.model.Listdb;
import com.example.td_wang_yang_wei.api.Items;
import com.example.td_wang_yang_wei.api.Lists;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public Listdb listfrom(Lists.ListsBean listsBean, String userId) {
        Listdb list = new Listdb();
        list.setId(listsBean.getId());
        list.setLabel(listsBean.getLabel());
        list.setUserId(userId);
        return list;
    }

    public List<Listdb> listsfrom(Lists lists,String userId) {
        List<Listdb> listsSave = new ArrayList<>(lists.getLists().size());
        for (Lists.ListsBean listsBean : lists.getLists()) {
            listsSave.add(listfrom(listsBean,userId));
        }
        return listsSave;
    }

    public Itemdb itemfrom(Items.ItemsBean itemsBean) {
        Itemdb item = new Itemdb();
        item.setId(itemsBean.getId());
        item.setLabel(itemsBean.getLabel());
        item.setChecked(itemsBean.getChecked());

        return item;
    }

    public List<Itemdb> itemsfrom(Items items) {
        List<Itemdb> itemsSave = new ArrayList<>(items.getItems().size());
        for (Items.ItemsBean itemsBean : items.getItems()) {
            itemsSave.add(itemfrom(itemsBean));
        }
        return itemsSave;
    }
}
