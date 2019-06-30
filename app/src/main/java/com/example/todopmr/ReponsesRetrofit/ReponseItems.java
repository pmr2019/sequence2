package com.example.todopmr.ReponsesRetrofit;

import com.example.todopmr.Modele.ItemToDo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReponseItems extends ReponseDeBase {

    @SerializedName("items")
    public List<ItemToDo> items;

}
