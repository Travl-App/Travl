package com.travl.guide.mvp.model.api.places.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceContainer {

    @SerializedName("data")
    @Expose
    private List<PlaceLink> placeLinkList;

    public List<PlaceLink> getPlaceLinkList() {
        return placeLinkList;
    }

    public void setPlaceLinkList(List<PlaceLink> placeLinkList) {
        this.placeLinkList = placeLinkList;
    }
}
