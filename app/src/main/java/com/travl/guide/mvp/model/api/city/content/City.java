package com.travl.guide.mvp.model.api.city.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.travl.guide.mvp.model.api.articles.CityArticleLinksContainer;

public class City {
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("image")
	@Expose
	private String image;
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
    private CityArticleLinksContainer articleLinksContainer;

    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("coordinates")
    @Expose
    private double[] coordinates;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

    public CityArticleLinksContainer getArticleLinksContainer() {
        return articleLinksContainer;
    }

    public void setArticleLinksContainer(CityArticleLinksContainer articleLinksContainer) {
        this.articleLinksContainer = articleLinksContainer;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
