package com.travl.guide.mvp.model.api.city.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.travl.guide.mvp.model.api.articles.ArticleLink;

import java.util.List;

public class City {
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("place")
    @Expose
    private String placeName;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("articles")
    @Expose
    private List<ArticleLink> articleLinks;

    public City(String placeName, String region, String country) {
        this.placeName = placeName;
        this.region = region;
        this.country = country;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<ArticleLink> getArticleLinks() {
        return articleLinks;
    }

    public void setArticleLinks(List<ArticleLink> articleLinks) {
        this.articleLinks = articleLinks;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
