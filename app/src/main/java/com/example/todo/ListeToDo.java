package com.example.todo;


import java.io.Serializable;

public class ListeToDo implements Serializable {
    private String label;
    private Integer id;


    public ListeToDo(String titreListeToDo, Integer id) {
        this.label = titreListeToDo;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setIdListe(Integer idListe) {
        this.id = idListe;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ListeToDo{" +
                "label='" + label + '\'' +
                ", id=" + id +
                '}';
    }
}
