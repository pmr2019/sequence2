package com.todolist.MyRetrofit;

import com.google.gson.annotations.SerializedName;

public class Label {
    @SerializedName("id")
    public String id;

    @SerializedName("label")
    public String label;
}
