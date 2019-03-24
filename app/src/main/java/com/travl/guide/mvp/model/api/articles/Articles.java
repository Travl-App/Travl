package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Articles {
    @SerializedName("user")
    @Expose
    private String userName;
    @SerializedName("articles")
    @Expose
    private List<Article> articleList;

    public Articles(String userName, List<Article> articleList) {
        this.userName = userName;
        this.articleList = articleList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
