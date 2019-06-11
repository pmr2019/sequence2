package PMR.ToDoList.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ToDoList implements java.io.Serializable{

    private String nameToDoList;
    private ArrayList<Task> tasksList;
    private HashMap<UUID, Task> tasksMap;
    private UUID idList;

    public ToDoList () {
        tasksList = new ArrayList<Task>();
        this.idList = UUID.randomUUID();
        onDeserialization();
    }

    public ToDoList (String titreListeToDo, ArrayList<Task> lesItems) {
        this.nameToDoList = titreListeToDo;
        lesItems = new ArrayList<Task>();
        this.tasksList = lesItems;
        this.idList = UUID.randomUUID();
        onDeserialization();
    }

    public ToDoList(String titreListeToDo) {
        this.nameToDoList = titreListeToDo;
        tasksList = new ArrayList<Task>();
        this.idList = UUID.randomUUID();
        onDeserialization();
    }

    public String getNameToDoList() {
        return nameToDoList;
    }

    public ArrayList<Task> getLesItems() {
        return tasksList;
    }

    public UUID getIdList() {
        return idList;
    }

    public void setLesItems(ArrayList<Task> lesItems) {
        this.tasksList = lesItems;
    }

    public void ajouterItem(Task unItem)
    {
        this.tasksList.add(unItem);
    }

    public Boolean validerItem(String s)
    {
        int indice = -1;

        if ((indice = rechercherItem(s)) >=0)
        {
            this.tasksList.get(indice).setFait(Boolean.TRUE);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    public int rechercherItem(String s)
    {
        int retour = -1;
        Boolean trouve = Boolean.FALSE;
        for (int i=0; i < this.tasksList.size() ;i++)
        {
            if (this.tasksList.get(i).getDescription() == s)
            {
                retour=i;
                i=this.tasksList.size();
                trouve=Boolean.TRUE;
            }
        }
        return retour;
    }

    public void onDeserialization() {
        tasksMap = new HashMap<>();
        for (Task task : tasksList) {
            tasksMap.put(task.getIdTask(), task);
        }
    }

    @Override
    public String toString() {
        String retour;
        retour = "Liste : " + this.getNameToDoList()+ "Items : " + this.getLesItems().toString();
        return retour;
    }
}