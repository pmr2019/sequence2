package com.example.todolist.data.API;

import com.example.todolist.data.Item;
import com.google.gson.annotations.SerializedName;

public class ResponseItem extends ResponseBasic {

    @SerializedName("item")
    public Item item;
}
