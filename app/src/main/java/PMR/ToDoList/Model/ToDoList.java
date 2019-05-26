package PMR.ToDoList.Model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class ToDoList {

    private String nameToDoList;
    private List<Task> tasks;

    public ToDoList () {
        tasks = new ArrayList<Task>();
    }

    public ToDoList (String titreListeToDo, List<Task> lesItems) {
        this.nameToDoList = titreListeToDo;
        lesItems = new ArrayList<Task>();
        this.tasks = lesItems;
    }

    public ToDoList(String titreListeToDo) {
        this.nameToDoList = titreListeToDo;
        tasks = new ArrayList<Task>();
    }

    public String getTitreListeToDo() {
        return nameToDoList;
    }

    public void setTitreListeToDo(String titreListeToDo) {
        this.nameToDoList = titreListeToDo;
    }

    public List<Task> getLesItems() {
        return tasks;
    }

    public void setLesItems(List<Task> lesItems) {
        this.tasks = lesItems;
    }
    public void ajouterItem(Task unItem)
    {
        this.tasks.add(unItem);
    }
    public Boolean validerItem(String s)
    {
        int indice = -1;

        if ((indice = rechercherItem(s)) >=0)
        {
            this.tasks.get(indice).setFait(Boolean.TRUE);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    public int rechercherItem(String s)
    {
        int retour = -1;
        Boolean trouve = Boolean.FALSE;
        for (int i=0; i < this.tasks.size() ;i++)
        {
            if (this.tasks.get(i).getDescription() == s)
            {
                retour=i;
                i=this.tasks.size();
                trouve=Boolean.TRUE;
            }
        }
        return retour;
    }
    @Override
    public String toString() {
        String retour;
        retour = "Liste : " + this.getTitreListeToDo()+ "Items : " + this.getLesItems().toString();
        return retour;
    }
}