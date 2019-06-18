package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article {
    @Expose
    @SerializedName("subtitle")
    private String subtitle;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("image_cover")
    private String coverUrl;

    @Expose
    @SerializedName("article_places")
    private List<ArticlePlace> articlePlaces;

    @Expose
    private String modified;
    @Expose
    private List<Category> categories;
    @Expose
    private Author author;
    @Expose
    private String link;

    public List<ArticlePlace> getArticlePlaces() {
        return articlePlaces;
    }

    public void setArticlePlaces(List<ArticlePlace> articlePlaces) {
        this.articlePlaces = articlePlaces;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
