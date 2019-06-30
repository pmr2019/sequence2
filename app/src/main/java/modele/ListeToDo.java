package modele;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListeToDo implements Serializable {
    private static final String TAG = "ListeToDo";
    private String titreListeToDo;
    private List<ItemToDo> lesItems;

    public ListeToDo(){
        this.titreListeToDo = "Liste quelconques";
        this.lesItems = new ArrayList<>();
    }

    public ListeToDo(String titreListeToDo){
        this();
        if (titreListeToDo != null)
            this.titreListeToDo=titreListeToDo;
    }

    public String getTitreListeToDo() {
        return titreListeToDo;
    }

    public List<ItemToDo> getLesItems() {
        return lesItems;
    }

    public void setTitreListeToDo(String titreListeToDo) {
        if (titreListeToDo == null) return;
        this.titreListeToDo = titreListeToDo;
    }

    public void setLesItems(List<ItemToDo> lesItems) {
        if (lesItems == null) return;
        this.lesItems = lesItems;
    }

    public boolean ajouterItem(ItemToDo unItem){
        if (unItem == null) return false;
        this.lesItems.add(unItem);
        return true;
    }

    public ItemToDo rechercherItem(String description) {
        for (ItemToDo i : lesItems) {
            if (description == i.getDescription())
                return i;
        }
        Log.i(TAG, "Aucun item correspondant à la description");
        return null;
    }

    @Override
    public String toString(){
        String s;
        s = titreListeToDo ;
        return s;
    }
}
