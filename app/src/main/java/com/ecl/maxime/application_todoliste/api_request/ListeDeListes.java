package com.ecl.maxime.application_todoliste.api_request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by Max on 2019-06-17.
 */
public class ListeDeListes {

    @SerializedName("lists")
    public ArrayList<Liste> lists;
}
