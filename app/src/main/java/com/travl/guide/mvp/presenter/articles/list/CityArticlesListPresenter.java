package com.travl.guide.mvp.presenter.articles.list;

import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.view.articles.list.CityArticlesItemView;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public interface CityArticlesListPresenter {
    PublishSubject<CityArticlesItemView> getClickSubject(int position);

    void bindView(CityArticlesItemView itemView);

    int getListCount();

    void setArticleLinkList(List<ArticleLink> articleLinks);
}
