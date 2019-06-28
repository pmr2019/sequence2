package com.example.todolist.data.API;

import com.example.todolist.data.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseItems extends ResponseBasic {

    @SerializedName("items")
    public List<Item> items;
}
