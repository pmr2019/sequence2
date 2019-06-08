package com.todolist.MyRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lists {
    @SerializedName("version")
    public int version;

    @SerializedName("success")
    public boolean success;

    @SerializedName("status")
    public int status;

    @SerializedName("labels")
    public List<Label> labels;
}
