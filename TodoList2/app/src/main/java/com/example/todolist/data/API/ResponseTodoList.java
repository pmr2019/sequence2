package com.example.todolist.data.API;

import com.example.todolist.data.TodoList;
import com.google.gson.annotations.SerializedName;

public class ResponseTodoList extends ResponseBasic {

    @SerializedName("list")
    public TodoList list;
}
