package com.travl.guide.mvp.presenter.articles.list;

import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.view.articles.list.TravlZineArticlesItemView;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public interface TravlZineArticlesListPresenter {
    PublishSubject<TravlZineArticlesItemView> getClickSubject(int position);

    void bindView(TravlZineArticlesItemView itemView);

    int getListCount();

    void setArticleLinkList(List<ArticleLink> articleLinks);
}