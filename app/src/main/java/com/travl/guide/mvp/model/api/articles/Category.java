package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("name")
    @Expose
    private String name;

    @Expose
    private int id;
    @Expose
    private String modified;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
