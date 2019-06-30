package com.ecl.maxime.application_todoliste.api_request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Max on 2019-06-17.
 */
public class ItemResponse {

    @SerializedName("id")
    private long id;

    @SerializedName("label")
    private String label;

    @SerializedName("url")
    private Object url;

    @SerializedName("checked")
    private boolean checked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
