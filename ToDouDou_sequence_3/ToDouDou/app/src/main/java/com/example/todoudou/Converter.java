package com.example.todoudou;

import com.example.todoudou.database.ItemToDo;
import com.example.todoudou.database.ListeToDo;
import com.google.gson.JsonObject;

import java.util.List;

public class Converter {

    // à partir de l'objet JSON "list" renvoyé par l'API, créé une nouvelle liste
    public ListeToDo ListFromJson(JsonObject list){
        int id = list.get("id").getAsInt();
        String description = list.get("label").getAsString();
        return new ListeToDo(description, id);
    }

    // à partir de l'objet JSON item renvoyé par l'API, créé un nouvel item
    public ItemToDo ItemFromJson(JsonObject item) {
        int id = item.get("id").getAsInt();
        String description = item.get("label").getAsString();
        boolean fait = true;
        if (item.get("checked").getAsInt() == 0) fait = false;
        return new ItemToDo(description, fait, id);
    }

    public void copyToFromList(List<ListeToDo> refList, List<ListeToDo> list){
        refList.clear();
        for(int k = 0 ; k < list.size() ; k++){
            refList.add(list.get(k));
        }
    }

    public void copyToFromItem(List<ItemToDo> refItems, List<ItemToDo> items){
        refItems.clear();
        for(int k = 0 ; k < items.size() ; k++){
            refItems.add(items.get(k));
        }
    }

}