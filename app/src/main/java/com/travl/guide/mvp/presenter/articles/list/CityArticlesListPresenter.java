package com.travl.guide.mvp.presenter.articles.list;

import com.travl.guide.mvp.model.api.articles.Article;
import com.travl.guide.mvp.view.articles.list.TravlZineArticlesItemView;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public interface CityArticlesListPresenter {
    PublishSubject<TravlZineArticlesItemView> getClickSubject();

    void bindView(TravlZineArticlesItemView itemView);

    int getListCount();

    void setArticleList(List<Article> articles);
}
