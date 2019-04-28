package com.travl.guide.mvp.model.user;

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

    public double[] getCoordinates() {
        if (coordinates == null) coordinates = new double[]{0, 0};
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getDefaultUserName() {
        return "travl";
    }
}
