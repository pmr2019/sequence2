package com.example.todo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "items")
public class ItemToDo implements Serializable {
    private String label;
    private Integer checked;
    @PrimaryKey private Integer id;
    private String url;

    private Integer idListe;

   /*
    public ItemToDo(String label, Integer checked, Integer id, String url) {
        this.label = label;
        this.checked = checked;
        this.id = id;
        this.url = url;
    }
    */

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

    public Integer getIdListe() {
        return idListe;
    }

    public void setIdListe(Integer idListe) {
        this.idListe = idListe;
    }

    public ItemToDo(String label, Integer checked, Integer id, String url, Integer idListe) {
        this.label = label;
        this.checked = checked;
        this.id = id;
        this.url = url;
        this.idListe = idListe;
    }
}
