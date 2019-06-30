package com.example.todopmr.ReponsesRetrofit;

import com.example.todopmr.Modele.ProfilListeToDo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReponseUsers extends ReponseDeBase {

    @SerializedName("users")
    public List<ProfilListeToDo> users;

}
