package com.travl.guide.mvp.model.api.newPlaces;

import com.google.gson.annotations.Expose;

public class Root {
    @Expose
    private int status;
    @Expose
    private Place place;
    @Expose
    private String user;

    public Root() {
    }

    public Root(int status, Place place, String user) {
        this.status = status;
        this.place = place;
        this.user = user;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Place getPlace() {
        return this.place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
