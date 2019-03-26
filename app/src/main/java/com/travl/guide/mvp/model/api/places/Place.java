package com.travl.guide.mvp.model.api.places;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Feature;

import java.util.List;

public class Place {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("coordinates")
    @Expose
    private double[] coordinates;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("images")
    @Expose
    private List<String> imageUrls;

    private View view;
    private Feature feature;

    public Place() {
    }

    public Place(int id, double[] coordinates, String description, String author, Feature feature) {
        this.id = id;
        this.description = description;
        this.coordinates = coordinates;
        this.author = author;
        this.feature = feature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}