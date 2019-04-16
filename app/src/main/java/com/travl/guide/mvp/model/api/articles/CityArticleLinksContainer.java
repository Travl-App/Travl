package com.travl.guide.mvp.model.api.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityArticleLinksContainer {
    @SerializedName("data")
    @Expose
    private List<ArticleLink> articleLinkList;

    public List<ArticleLink> getArticleLinkList() {
        return articleLinkList;
    }

    public void setArticleLinkList(List<ArticleLink> articleLinkList) {
        this.articleLinkList = articleLinkList;
    }
}
