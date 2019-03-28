package com.travl.guide.mvp.model.api.author;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author {
    @SerializedName("username")
    @Expose
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
