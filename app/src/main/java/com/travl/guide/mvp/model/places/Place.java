package com.travl.guide.mvp.model.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("coordinates")
    @Expose
    private double[] coordinates;
    private String position;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public Place() {
    }

    public Place(int id, String title, double[] coordinates, String imageUrl) {
        this.id = id;
        this.title = title;
        this.coordinates = coordinates;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosition() {
        if (position == null) {
            position = coordinates[0] + "," + coordinates[1];
        }
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}