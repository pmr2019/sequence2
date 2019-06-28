package com.ecl.maxime.application_todoliste.api_request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Max on 2019-06-17.
 */
public class Item {

    @SerializedName("id")
    private String id;

    @SerializedName("label")
    private String label;

    @SerializedName("url")
    private Object url;

    @SerializedName("checked")
    private boolean checked;

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

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
