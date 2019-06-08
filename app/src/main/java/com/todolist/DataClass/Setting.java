package com.todolist.DataClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Setting implements Serializable {
    private final String URL = "http://tomnab.fr/todo-api/";
    private String url;
    private List<User> users;

    public Setting() {
        this.url = URL; //default api setting
        this.users = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public User getLastUser() {
        if (users.isEmpty()) {
            return null;
        }
        return this.users.get(users.size() - 1);
    }

    public Boolean hasUser(String pseudo) {
        for (User u : users) {
            if (u.getPseudo().equals(pseudo)) return true;
        }
        return false;
    }

    public User getUser(String pseudo) {
        for (User u : users) {
            if (u.getPseudo().equals(pseudo)) return u;
        }
        return null;
    }
}
