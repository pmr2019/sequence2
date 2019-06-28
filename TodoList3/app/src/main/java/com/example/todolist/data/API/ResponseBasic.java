package com.example.todolist.data.API;

import com.google.gson.annotations.SerializedName;

public class ResponseBasic {

    @SerializedName("version")
    public String version;

    @SerializedName("success")
    public boolean success;

    @SerializedName("status")
    public String status;
}
