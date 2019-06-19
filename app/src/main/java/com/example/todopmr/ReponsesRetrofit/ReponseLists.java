package com.example.todopmr.ReponsesRetrofit;

import com.example.todopmr.Modele.ListeToDo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReponseLists extends ReponseDeBase {

    @SerializedName("lists")
    public List<ListeToDo> lists;

}
