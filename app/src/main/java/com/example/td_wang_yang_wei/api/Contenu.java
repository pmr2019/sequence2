package com.example.td_wang_yang_wei.api;
import com.google.gson.annotations.SerializedName;

public class Contenu {
    @SerializedName("version")
    public int version;

    @SerializedName("success")
    public boolean success;

    @SerializedName("status")
    public int status;

    @SerializedName("hash")
    public String hash;
}

