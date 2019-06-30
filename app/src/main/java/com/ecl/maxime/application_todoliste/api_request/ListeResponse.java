package com.ecl.maxime.application_todoliste.api_request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Max on 2019-06-17.
 */
public class ListeResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("label")
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
