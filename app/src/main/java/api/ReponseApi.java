package api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import modele.ItemToDo;
import modele.ListeToDo;
import modele.ProfilListeToDo;

public class ReponseApi {

    @SerializedName("version")
    public String version;

    @SerializedName("sucess")
    public Boolean success;

    @SerializedName("status")
    public String status;

    @SerializedName("hash")
    public String hash;

    @SerializedName("user")
    public ProfilListeToDo user;

    @SerializedName("list")
    public ListeToDo list;

    @SerializedName("list")
    public List<ListeToDo> lists;

    @SerializedName("items")
    public ItemToDo items;



}
