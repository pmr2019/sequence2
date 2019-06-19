package com.example.todopmr.ReponsesRetrofit;

import com.google.gson.annotations.SerializedName;

public class ReponseDeBase {

    @SerializedName("version")
    public String version;

    @SerializedName("success")
    public Boolean success;

    @SerializedName("status")
    public String status;

}
