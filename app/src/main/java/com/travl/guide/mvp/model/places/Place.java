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