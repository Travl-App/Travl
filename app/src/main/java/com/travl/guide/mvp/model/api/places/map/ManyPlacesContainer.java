package com.travl.guide.mvp.model.api.places.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManyPlacesContainer {
    @SerializedName("places")
    @Expose
    private PlaceContainer places;
    @SerializedName("user")
    @Expose
    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PlaceContainer getPlaces() {
        return places;
    }

    public void setPlaces(PlaceContainer places) {
        this.places = places;
    }
}
