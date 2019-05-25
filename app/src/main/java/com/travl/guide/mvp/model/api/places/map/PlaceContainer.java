package com.travl.guide.mvp.model.api.places.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceContainer {

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    @Expose
    private List<PlaceLink> placeLinkList;

    @SerializedName("next")
    @Expose
    private String next;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<PlaceLink> getPlaceLinkList() {
        return placeLinkList;
    }

    public void setPlaceLinkList(List<PlaceLink> placeLinkList) {
        this.placeLinkList = placeLinkList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
