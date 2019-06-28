package com.example.todolist.data;

import com.example.todolist.data.API.ResponseItems;
import com.example.todolist.data.API.ResponseTodoLists;
import com.example.todolist.data.database.DatabaseItem;
import com.example.todolist.data.database.DatabaseProfile;
import com.example.todolist.data.database.DatabaseTodoList;

import java.util.ArrayList;
import java.util.List;

public class Convert {

    public DatabaseProfile profile(String name, String password) {
        DatabaseProfile profile = new DatabaseProfile();
        profile.mUserName = name;
        profile.mPassword = password;
        return profile;
    }

    public List<DatabaseTodoList> todoListList(ResponseTodoLists responseTodoLists, long idUser) {
        List<DatabaseTodoList> todoListList = new ArrayList<>();
        for (TodoList todoList : responseTodoLists.lists) {
            DatabaseTodoList databaseTodoList = new DatabaseTodoList();
            databaseTodoList.id = todoList.getId();
            databaseTodoList.idUser = idUser;
            databaseTodoList.title = todoList.getTitle();
            todoListList.add(databaseTodoList);
        }
        return todoListList;
    }

    public List<DatabaseItem> itemList(ResponseItems responseItems, long idList) {
        List<DatabaseItem> itemList = new ArrayList<>();
        for (Item item : responseItems.items) {
            DatabaseItem databaseItem = new DatabaseItem();
            databaseItem.id = item.getId();
            databaseItem.description = item.getDescription();
            databaseItem.done = item.getDone();
            databaseItem.idList = idList;
            itemList.add(databaseItem);
        }
        return itemList;
    }

    public List<TodoList> todoListListDB(List<DatabaseTodoList> databaseTodoList) {
        List<TodoList> todoListList = new ArrayList<>();
        for (DatabaseTodoList todoList : databaseTodoList) {
            TodoList list = new TodoList();
            list.setId(todoList.id);
            list.setTitle(todoList.title);
            todoListList.add(list);
        }
        return todoListList;
    }

    public List<Item> itemListDB(List<DatabaseItem> databaseItems) {
        List<Item> itemList = new ArrayList<>();
        for (DatabaseItem item : databaseItems) {
            Item i = new Item();
            i.setId(item.id);
            i.setDescription(item.description);
            i.setDone(item.done);
            itemList.add(i);
        }
        return itemList;
    }
}
