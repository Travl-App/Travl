package com.travl.guide.mvp.model.api.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceContainer {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("place")
    @Expose
    private Place place;

    public PlaceContainer(int status, Place place) {
        this.status = status;
        this.place = place;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
