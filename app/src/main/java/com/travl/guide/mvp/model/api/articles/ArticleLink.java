package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleLink {

    @SerializedName("categories")
    @Expose
    private List<Category> categories;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image_cover")
    @Expose
    private String imageCoverUrl;

    @SerializedName("link")
    @Expose
    private String link;


    public ArticleLink(String title, String imageCoverUrl) {
        this.title = title;
        this.imageCoverUrl = imageCoverUrl;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageCoverUrl() {
        return imageCoverUrl;
    }

    public void setImageCoverUrl(String imageCoverUrl) {
        this.imageCoverUrl = imageCoverUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
