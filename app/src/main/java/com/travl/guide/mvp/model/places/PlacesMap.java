package com.travl.guide.mvp.model.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesMap {
    @SerializedName("user")
    @Expose
    private String name;
    @SerializedName("places")
    @Expose
    private List<Place> places;

    public PlacesMap(String name, List<Place> places) {
        this.name = name;
        this.places = places;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
