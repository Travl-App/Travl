package com.travl.guide.mvp.model.network;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CoordinatesRequest {
    private double longitude;
    private double latitude;

    public CoordinatesRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CoordinatesRequest(double[] coordinates) {
        if (coordinates != null) {
            setLatitude(coordinates[0]);
            setLongitude(coordinates[1]);
        }
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.10f,%.10f", latitude, longitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
