package PMR.ToDoList.Model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class User {

    private List<ToDoList> ToDoLists;
    private String login;

    public User(List<ToDoList> mesListeToDo, String login) {
        this.ToDoLists = mesListeToDo;
        this.login = login;
    }

    public User(String login) {
        this.login = login;
        this.ToDoLists = new ArrayList<>();
    }

    public User(List<ToDoList> mesListeToDo) {
        this.ToDoLists = mesListeToDo;
    }

    public List<ToDoList> getMesListeToDo() {
        return ToDoLists;
    }

    public void setMesListeToDo(List<ToDoList> mesListeToDo) {
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


    public void ajouteListe(ToDoList uneListe)
    {
        this.ToDoLists.add(uneListe);
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + ToDoLists + ", login=" + login + '}';
    }


}
