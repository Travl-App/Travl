package com.travl.guide.mvp.model.api.newPlaces;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Place {
    @Expose
    private int id;
    @Expose
    private Author author;
    @Expose
    private List<String> images;
    @Expose
    private List<Double> coordinates;
    @Expose
    private String link;
    @Expose
    private List<Articles> articles;
    @Expose
    private String title;
    @Expose
    private String modified;
    @Expose
    private List<Categories> categories;
    @Expose
    private String description;

    public Place() {
    }

    public Place(int id, Author author, List<String> images, List<Double> coordinates, String link, List<Articles> articles, String title, String modified, List<Categories> categories, String description) {
        this.id = id;
        this.author = author;
        this.images = images;
        this.coordinates = coordinates;
        this.link = link;
        this.articles = articles;
        this.title = title;
        this.modified = modified;
        this.categories = categories;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Double> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Articles> getArticles() {
        return this.articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModified() {
        return this.modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<Categories> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
