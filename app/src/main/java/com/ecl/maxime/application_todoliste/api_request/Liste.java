package com.ecl.maxime.application_todoliste.api_request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Max on 2019-06-17.
 */
public class Liste {

    @SerializedName("id")
    private String id;

    @SerializedName("label")
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
