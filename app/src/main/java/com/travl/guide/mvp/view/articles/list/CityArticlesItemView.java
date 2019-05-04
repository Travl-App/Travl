package com.travl.guide.mvp.view.articles.list;

public interface CityArticlesItemView {
    int getPos();

    void setImage(String url);

    void setDescription(String title);

    void setAuthor(String author);
}
