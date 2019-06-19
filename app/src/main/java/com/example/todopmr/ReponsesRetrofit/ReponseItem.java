package com.example.todopmr.ReponsesRetrofit;

import com.example.todopmr.Modele.ItemToDo;
import com.google.gson.annotations.SerializedName;

public class ReponseItem extends ReponseDeBase {

    @SerializedName("item")
    public ItemToDo item;
}
