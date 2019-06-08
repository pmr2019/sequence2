package com.todolist.MyRetrofit;

import com.google.gson.annotations.SerializedName;

public class Hash {
    @SerializedName("version")
    public int version;

    @SerializedName("success")
    public boolean success;

    @SerializedName("status")
    public int status;

    @SerializedName("hash")
    public String hash;
}
