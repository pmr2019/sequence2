package PMR.ToDoList.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ToDoList implements Parcelable {

    private String nameToDoList;
    private ArrayList<Task> tasksList;

    public ToDoList (String titreListeToDo, ArrayList<Task> lesItems) {
        this.nameToDoList = titreListeToDo;
        this.tasksList = lesItems;
    }

    public ToDoList(String titreListeToDo) {
        this.nameToDoList = titreListeToDo;
        tasksList = new ArrayList<Task>();
    }

    protected ToDoList(Parcel in) {
        nameToDoList = in.readString();
        tasksList = in.createTypedArrayList(Task.CREATOR);
    }

    public static final Creator<ToDoList> CREATOR = new Creator<ToDoList>() {
        @Override
        public ToDoList createFromParcel(Parcel in) {
            return new ToDoList(in);
        }

        @Override
        public ToDoList[] newArray(int size) {
            return new ToDoList[size];
        }
    };

    public String getNameToDoList() {
        return nameToDoList;
    }

    public ArrayList<Task> getLesItems() {
        return tasksList;
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

    @Override
    public String toString() {
        String retour;
        retour = "Liste : " + this.getNameToDoList()+ "Items : " + this.getLesItems().toString();
        return retour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameToDoList);

        dest.writeTypedList(this.tasksList);
    }
}