package PMR.ToDoList.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class User implements Parcelable {

    private ArrayList<ToDoList> ToDoLists;
    private String login;

    public User(ArrayList<ToDoList> mesListeToDo, String login) {
        this.ToDoLists = mesListeToDo;
        this.login = login;
    }

    public User(String login) {
        this.login = login;
        this.ToDoLists = new ArrayList<>();
    }

    public User(ArrayList<ToDoList> mesListeToDo) {
        this.ToDoLists = mesListeToDo;
    }

    protected User(Parcel in) {
        login = in.readString();
        ToDoLists = new ArrayList<>();
        in.readList(ToDoLists, ToDoList.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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


    public void ajouteListe(ToDoList uneListe)
    {
        this.ToDoLists.add(uneListe);
    }

    @Override
    public String toString() {
        return "ProfilListeToDo{" + "mesListeToDo=" + ToDoLists + ", login=" + login + '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(login);
        parcel.writeList(ToDoLists);
    }
}
