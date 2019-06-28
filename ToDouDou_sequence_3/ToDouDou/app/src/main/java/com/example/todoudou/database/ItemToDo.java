package com.example.todoudou.database;


import java.io.Serializable;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todoudou.database.ListeToDo;


@Entity
public class ItemToDo implements Serializable {
    @ForeignKey(entity = ListeToDo.class,parentColumns = "uid",childColumns = "idliste")
    private int idliste;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "fait")
    private Boolean fait;

    @ColumnInfo(name = "id")
    private int id = 0;

    @PrimaryKey(autoGenerate = true)
    private int uid = 0;

    @ColumnInfo(name = "modifie")
    private Boolean modifie;




    @Ignore
    public ItemToDo(String description, Boolean fait, int id) {
        this.description = description;
        this.fait = fait;
        this.id = id;
        this.modifie = false;
    }

    public ItemToDo(String description, Boolean fait, int id, int idliste) {
        this.description = description;
        this.fait = fait;
        this.id = id;
        this.idliste = idliste;
        this.modifie = false;
    }

    @Ignore
    public ItemToDo(String description, int idliste) {
        this.description = description;
        this.idliste = idliste;
        this.fait = false;
        this.modifie = false;
    }

    public int getUid() {return this.uid;}

    public int getId(){
        return this.id;
    }

    public int getIdliste() {
        return idliste;
    }

    public void setIdliste(int idliste) {
        this.idliste = idliste;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFait() {
        return fait;
    }

    public void setFait(Boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return "ItemToDo{" +
                "idliste=" + idliste +
                ", description='" + description + '\'' +
                ", fait=" + fait +
                ", id=" + id +
                ", uid=" + uid +
                ", modif=" + modifie +
                '}';
    }


    // si this n'est pas dans la list, la fonction renvoie -1
    // sinon elle renvoie l'index de la list telle que list.get(index) == this
    // le critère d'égalité entre 2 items est l'id "id" (celui de l'API)
    public int estDans(List<ItemToDo> list){
        int id = this.getId();
        for(int k = 0 ; k < list.size() ; k++){
            if(list.get(k).getId() == id)
                return k;
        }
        return -1;
    }

    public Boolean getModifie() {
        return modifie;
    }

    public void setModifie(Boolean modifie) {
        this.modifie = modifie;
    }


//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this)
//            return true;
//        if (getClass() != obj.getClass())
//            return false;
//        ItemToDo pn = (ItemToDo) obj;
//        return pn.id == id && pn.fait == (fait);
//    }

}