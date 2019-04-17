package com.travl.guide.mvp.model.api.places.articles;

import com.google.gson.annotations.Expose;

public class Author {
    @Expose
    private String username;
    @Expose
    private String modified;
    @Expose
    private String link;
    @Expose
    private boolean is_active;

    public Author() {
    }

    public Author(String username, String modified, String link, boolean is_active) {
        this.username = username;
        this.modified = modified;
        this.link = link;
        this.is_active = is_active;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean getIs_active() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
