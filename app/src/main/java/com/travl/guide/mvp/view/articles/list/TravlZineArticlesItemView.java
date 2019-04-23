package com.travl.guide.mvp.view.articles.list;

public interface TravlZineArticlesItemView {
    int getPos();

    void setImage(String url);

    void setDescription(String title);

    void setCategory(String category);
}