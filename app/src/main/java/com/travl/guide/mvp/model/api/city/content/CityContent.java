package com.travl.guide.mvp.model.api.city.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityContent {
    @SerializedName("user")
    @Expose
    private String userName;


    @SerializedName("city")
    @Expose
    private City city;


    @SerializedName("context")
    @Expose
    private City context;


    public CityContent(String userName, City city) {
        this.userName = userName;
        this.city = city;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getContext() {
        return context;
    }

    public void setContext(City context) {
        this.context = context;
    }
}
