package com.travl.guide.mvp.model.places;

public class PlaceEntity {

    private String title;
    private String subtitle;
    private String imageUrl;
    private String placeAddress;
    private String authorName;
    private String text;

    public PlaceEntity() {
    }

    public PlaceEntity(String title, String subtitle, String imageUrl, String placeAddress, String authorName, String text) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.placeAddress = placeAddress;
        this.authorName = authorName;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
