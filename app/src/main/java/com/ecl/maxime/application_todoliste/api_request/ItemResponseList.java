package com.ecl.maxime.application_todoliste.api_request;

import com.ecl.maxime.application_todoliste.data.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Max on 2019-06-17.
 */
public class ItemResponseList {

    @SerializedName("mItemResponses")
    public List<ItemResponse> items;
}
