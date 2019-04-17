package com.travl.guide.mvp.model.api.places.map;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Feature;
import com.travl.guide.mvp.model.api.author.Author;

import java.util.List;

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

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("images")
    @Expose
    private List<String> imageUrls;

    transient private View view;
    transient private Feature feature;

    public Place() {
    }

    public Place(int id, String title, double[] coordinates, String description, Author author, Feature feature) {
        this.id = id;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
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