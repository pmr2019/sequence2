package com.example.todolist.data.API;

import com.example.todolist.data.TodoList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTodoLists extends ResponseBasic {

    @SerializedName("lists")
    public List<TodoList> lists;
}
