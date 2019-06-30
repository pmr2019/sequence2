package com.ecl.maxime.application_todoliste.api_request;

import com.ecl.maxime.application_todoliste.data.Liste;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Max on 2019-06-17.
 */
public class ListeResponseList {

    @SerializedName("lists")
    public List<ListeResponse> lists;
}
