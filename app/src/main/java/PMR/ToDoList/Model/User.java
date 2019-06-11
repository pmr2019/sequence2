package PMR.ToDoList.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import java.util.UUID;

public class User implements Parcelable {

    private String pseudo;
    private String password;
    private String hash;
    private ArrayList<ToDoList> ToDoLists;
    //private HashMap<UUID, ToDoList> ToDoListMap;

    public User(String pseudo, String password) {
        this.pseudo = pseudo;
        this.password = password;
    }

    protected User(Parcel in) {
        pseudo = in.readString();
        password = in.readString();
        hash = in.readString();
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

    public String getPseudo() {
        return pseudo;
    }

    public String getHash() {
        return hash;
    }

    public ArrayList<ToDoList> getToDoLists() {
        return ToDoLists;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setToDoLists(ArrayList<ToDoList> toDoLists) {
        ToDoLists = toDoLists;
    }

    /* Cette méthode va permettre de recréer la HashMap des ToDoLists associées au user, sans
    quelles n'aient été stockées dans le fichier Json
     */

    /*public void onDeserialization() {
        ToDoListMap = new HashMap<>();
        for (ToDoList tdl : ToDoLists) {
            ToDoListMap.put(tdl.getIdList(), tdl);
        }
    }
    */

    public void ajouteListe(ToDoList uneListe)
    {
        this.ToDoLists.add(uneListe);
    }

    /*
    public void supprimeListe(UUID idList)
    {
        this.ToDoLists.remove(ToDoListMap.get(idList));
    }

    */

    @Override
    public String toString() {
        return "User{" +
                "pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pseudo);
        dest.writeString(this.password);
        dest.writeString(this.hash);

        dest.writeTypedList(this.ToDoLists);
    }
}
