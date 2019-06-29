package PMR.ToDoList.data.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "user_table")
public class User implements Parcelable {


    @PrimaryKey(autoGenerate = false)
    private int idUser;
    private String pseudo;
    private String password;
    private String hash;
    @Ignore
    private ArrayList<ToDoList> toDoLists;

    // CONSTRUCTEURS

    public User(String pseudo, String password, String hash) {
        this.pseudo = pseudo;
        this.password = password;
        this.hash=hash;
        this.toDoLists=new ArrayList<>();
    }

    public User(User user) {
        this.pseudo = user.pseudo;
        this.password = user.password;
        this.hash=user.hash;
        this.toDoLists=new ArrayList<>();
    }

    protected User(Parcel in) {
        pseudo = in.readString();
        password = in.readString();
        hash = in.readString();
        toDoLists = new ArrayList<>();
        in.readTypedList(this.toDoLists, ToDoList.CREATOR);
    }


    // GETTERS & SETTERS
    public int getIdUser() { return idUser;}

    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getPseudo() {
        return pseudo;
    }

    public String getPassword() { return password; }

    public String getHash() {
        return hash;
    }

    public ArrayList<ToDoList> getToDoLists() {
        return toDoLists;
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
        this.toDoLists = toDoLists;
    }

    // PARCELABLE IMPLEMENTATION
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pseudo);
        dest.writeString(this.password);
        dest.writeString(this.hash);

        dest.writeTypedList(this.toDoLists);
    }


    // METHODES UTILES

    public void ajouteListe(ToDoList uneListe)
    {
        this.toDoLists.add(uneListe);
    }


    public void supprimeListe(int id)
    {
        this.toDoLists.remove(id);
    }



    @Override
    public String toString() {
        return "User{" +
                "pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object user) {
        User otherUser= (User)user;
        if (this.pseudo.equals(otherUser.getPseudo()))
            if (this.password.equals(otherUser.getPassword()))
                return true;
        return false;
    }
}
