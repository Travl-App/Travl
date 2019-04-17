package com.travl.guide.mvp.model.api.places.articles;

import com.google.gson.annotations.Expose;

public class Articles {
    @Expose
    private int id;
    @Expose
    private String title;
    @Expose
    private String modified;
    @Expose
    private String link;

    public Articles() {
    }

    public Articles(int id, String title, String modified, String link) {
        this.id = id;
        this.title = title;
        this.modified = modified;
        this.link = link;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
