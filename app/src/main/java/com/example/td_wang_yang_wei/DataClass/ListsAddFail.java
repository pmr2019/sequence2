package com.example.td_wang_yang_wei.DataClass;

import com.example.td_wang_yang_wei.api.Lists;

import java.util.ArrayList;
import java.util.List;

public class ListsAddFail {
    private List<String> lists;

    ListsAddFail(){ lists = new ArrayList<>(); }

    public List<String> getLists(){return lists;}

    public void addList(String list){this.lists.add(list);}
}
