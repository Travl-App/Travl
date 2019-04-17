package com.travl.guide.mvp.model.api.places.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceLink {

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("coordinates")
    @Expose
    private double[] coordinates;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("images")
    @Expose
    private List<String> imageUrls;

    public PlaceLink(String link, int id, double[] coordinates, String description, List<String> imageUrls) {
        this.link = link;
        this.id = id;
        this.coordinates = coordinates;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
