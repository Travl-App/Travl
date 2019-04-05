package com.travl.guide.mvp.model.api.newPlaces;

import com.google.gson.annotations.Expose;

public class Categories {
    @Expose
    private int id;
    @Expose
    private String modified;
    @Expose
    private String name;
    @Expose
    private String link;

    public Categories() {
    }

    public Categories(int id, String modified, String name, String link) {
        this.id = id;
        this.modified = modified;
        this.name = name;
        this.link = link;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModified() {
        return this.modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
