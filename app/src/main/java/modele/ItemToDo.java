package modele;

import java.io.Serializable;

public class ItemToDo implements Serializable {
    private String description;
    private boolean fait = false;

    public ItemToDo(){
        this.description = "Item quelconque";
    }

    public ItemToDo(String description){
        this();
        this.description = description;
    }

    public ItemToDo(String description, boolean fait){
        this();
        this.description = description;
        this.fait = fait;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFait() {
        return fait;
    }

    public void setDescription(String description) {
        if (description == null) return;
        this.description = description;
    }

    public void setFait(boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString(){
        String s = description;

        return s;
    }
}
