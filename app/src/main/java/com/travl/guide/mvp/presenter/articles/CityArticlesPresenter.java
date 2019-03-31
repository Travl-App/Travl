package com.travl.guide.mvp.presenter.articles;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.Article;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.presenter.articles.list.CityArticlesListPresenter;
import com.travl.guide.mvp.view.articles.CityArticlesView;
import com.travl.guide.mvp.view.articles.list.CityArticlesItemView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@InjectViewState
public class CityArticlesPresenter extends MvpPresenter<CityArticlesView> {

    @Inject
    ArticlesRepo articlesRepo;
    @Inject
    @Named("baseUrl")
    String baseUrl;
    private Scheduler scheduler;
    public CityArticlesListPresenterImpl cityArticlesListPresenter;

    public CityArticlesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        this.cityArticlesListPresenter = new CityArticlesListPresenterImpl();
    }

    public void loadArticles() {
        cityArticlesListPresenter.setArticleList(new ArrayList<>());
    }

    public class CityArticlesListPresenterImpl implements CityArticlesListPresenter {
        PublishSubject<CityArticlesItemView> clickSubject = PublishSubject.create();
        private List<Article> articleList;

        @Override
        public PublishSubject<CityArticlesItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(CityArticlesItemView view) {
            Timber.d("BindView and set Description");
            Article article = articleList.get(view.getPos());
            view.setDescription(article.getTitle());
            view.setImage(baseUrl + article.getImageCoverUrl().substring(1));
        }

        @Override
        public int getListCount() {
            Timber.d("PlaceList size = %s", (articleList == null ? null : articleList.size()));
            return articleList == null ? 0 : articleList.size();
        }

        @Override
        public void setArticleList(List<Article> articles) {
            this.articleList = articles;
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            articles.add(new Article("test", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            getViewState().onChangedArticlesData();
        }
    }
}
