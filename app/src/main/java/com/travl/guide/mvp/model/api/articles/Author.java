package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author {
    @SerializedName("username")
    @Expose
    private String userName;

    @Expose
    private String modified;
    @Expose
    private String link;
    @Expose
    private String image;
    @Expose
    private boolean is_active;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
