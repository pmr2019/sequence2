package com.example.todopmr.ReponsesRetrofit;

import com.example.todopmr.Modele.ListeToDo;
import com.google.gson.annotations.SerializedName;

public class ReponseList extends ReponseDeBase {

    @SerializedName("list")
    public ListeToDo list;

}
