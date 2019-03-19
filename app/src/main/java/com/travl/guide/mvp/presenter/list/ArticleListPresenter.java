package com.travl.guide.mvp.presenter.list;

import com.travl.guide.mvp.model.api.articles.Article;
import com.travl.guide.mvp.view.list.ArticlesItemView;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

//Created by Pereved on 23.02.2019.
public interface ArticleListPresenter {
    PublishSubject<ArticlesItemView> getClickSubject();

    void bindView(ArticlesItemView itemView);

    int getListCount();

    void setArticleList(List<Article> articles);
}