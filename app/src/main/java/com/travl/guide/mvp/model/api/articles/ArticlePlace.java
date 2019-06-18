package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticlePlace {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("selected_image")
    @Expose
    private String placeImageUrl;

    @SerializedName("article_text")
    @Expose
    private String articleText;

    @Expose
    private String title;
    @Expose
    private String modified;
    @Expose
    private String link;
    @Expose
    private int order;
    @Expose
    private List<String> other_images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceImageUrl() {
        return placeImageUrl;
    }

    public void setPlaceImageUrl(String placeImageUrl) {
        this.placeImageUrl = placeImageUrl;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<String> getOther_images() {
        return other_images;
    }

    public void setOther_images(List<String> other_images) {
        this.other_images = other_images;
    }
}
