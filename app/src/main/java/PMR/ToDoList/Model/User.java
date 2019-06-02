package PMR.ToDoList.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private ArrayList<ToDoList> ToDoLists;
    private HashMap<UUID, ToDoList> ToDoListMap;
    private String login;

    public User(ArrayList<ToDoList> mesListeToDo, String login) {
        this.ToDoLists = mesListeToDo;
        this.login = login;
        onDeserialization();
    }

    public User(String login) {
        this.login = login;
        this.ToDoLists = new ArrayList<>();
        onDeserialization();
    }

    public ArrayList<ToDoList> getMesListeToDo() {
        return ToDoLists;
    }

    public void setMesListeToDo(ArrayList<ToDoList> mesListeToDo) {
        this.ToDoLists = mesListeToDo;
    }

    public String getLogin() {
        return login;
    }

    /*
    public void setLogin(String login) {
        this.login = login;
    }
    */

    /* Cette méthode va permettre de recréer la HashMap des ToDoLists associées au user, sans
    quelles n'aient été stockées dans le fichier Json
     */

    public void onDeserialization() {
        ToDoListMap = new HashMap<>();
        for (ToDoList tdl : ToDoLists) {
            ToDoListMap.put(tdl.getIdList(), tdl);
        }
    }

    public void ajouteListe(ToDoList uneListe)
    {
        this.ToDoLists.add(uneListe);
    }
    public void supprimeListe(UUID idList)
    {
        this.ToDoLists.remove(ToDoListMap.get(idList));
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + ToDoLists + ", login=" + login + '}';
    }
}
