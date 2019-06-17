package com.example.todo.API_models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetroMain {

    @SerializedName("version")
    private Integer version;
    @SerializedName("success")
    private boolean success;
    @SerializedName("status")
    private Integer status;

    @SerializedName("hash")
    private String hash;

    @SerializedName("user")
    private RetroMain user;
    @SerializedName("users")
    private ArrayList<RetroMain> users;
    @SerializedName("id")
    private Integer id;
    @SerializedName("pseudo")
    private String pseudo;

    @SerializedName("list")
    private RetroMain list;
    @SerializedName("lists")
    private ArrayList<RetroMain> lists;
    @SerializedName("label")
    private String label;

    @SerializedName("item")
    private RetroMain item;
    @SerializedName("items")
    private ArrayList<RetroMain> items;
    @SerializedName("url")
    private String url;
    @SerializedName("checked")
    private Integer checked;

    public RetroMain(Integer version, boolean success, Integer status, String hash, RetroMain user, ArrayList<RetroMain> users, Integer id, String pseudo, RetroMain list, ArrayList<RetroMain> lists, String label, RetroMain item, ArrayList<RetroMain> items, String url, Integer checked) {
        this.version = version;
        this.success = success;
        this.status = status;
        this.hash = hash;
        this.user = user;
        this.users = users;
        this.id = id;
        this.pseudo = pseudo;
        this.list = list;
        this.lists = lists;
        this.label = label;
        this.item = item;
        this.items = items;
        this.url = url;
        this.checked = checked;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public RetroMain getUser() {
        return user;
    }

    public void setUser(RetroMain user) {
        this.user = user;
    }

    public ArrayList<RetroMain> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<RetroMain> users) {
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public RetroMain getList() {
        return list;
    }

    public void setList(RetroMain list) {
        this.list = list;
    }

    public ArrayList<RetroMain> getLists() {
        return lists;
    }

    public void setLists(ArrayList<RetroMain> lists) {
        this.lists = lists;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public RetroMain getItem() {
        return item;
    }

    public void setItem(RetroMain item) {
        this.item = item;
    }

    public ArrayList<RetroMain> getItems() {
        return items;
    }

    public void setItems(ArrayList<RetroMain> items) {
        this.items = items;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }
}
