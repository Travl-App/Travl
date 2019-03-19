package com.travl.guide.mvp.model.api.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceLink {

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("id")
    @Expose
    private int id;

    public PlaceLink(String link, int id) {
        this.link = link;
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
