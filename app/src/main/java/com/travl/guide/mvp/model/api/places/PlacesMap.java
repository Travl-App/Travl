package com.travl.guide.mvp.model.api.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesMap {
    @SerializedName("places")
    @Expose
    private List<PlaceLink> places;
    @SerializedName("user")
    @Expose
    private String name;

    public PlacesMap(List<PlaceLink> places, String name) {
        this.name = name;
        this.places = places;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlaceLink> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceLink> places) {
        this.places = places;
    }
}
