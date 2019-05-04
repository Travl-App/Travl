package com.travl.guide.mvp.model.user;

import io.reactivex.annotations.Nullable;

public class User {
    private static User instance;
    // first item = latitude, second item = longitude
    private double[] coordinates;

    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    @Nullable
    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getDefaultUserName() {
        return "travl";
    }
}
