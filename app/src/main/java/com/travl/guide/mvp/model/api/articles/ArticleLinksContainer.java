package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleLinksContainer {
    @SerializedName("user")
    @Expose
    private String userName;
    @SerializedName("articles")
    @Expose
    private List<ArticleLink> articleLinkList;

    public ArticleLinksContainer(String userName, List<ArticleLink> articleLinkList) {
        this.userName = userName;
        this.articleLinkList = articleLinkList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ArticleLink> getArticleLinkList() {
        return articleLinkList;
    }

    public void setArticleLinkList(List<ArticleLink> articleLinkList) {
        this.articleLinkList = articleLinkList;
    }
}
