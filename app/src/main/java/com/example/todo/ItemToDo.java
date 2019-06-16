package com.example.todo;

import java.io.Serializable;

public class ItemToDo implements Serializable {
    private String label;
    private Integer checked;
    private Integer id;
    private String url;

    public ItemToDo(String label, Integer checked, Integer id, String url) {
        this.label = label;
        this.checked = checked;
        this.id = id;
        this.url = url;
    }

    @Override
    public String toString() {
        return "ItemToDo{" +
                "label='" + label + '\'' +
                ", checked=" + checked +
                ", id=" + id +
                ", url='" + url + '\'' +
                '}';
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
