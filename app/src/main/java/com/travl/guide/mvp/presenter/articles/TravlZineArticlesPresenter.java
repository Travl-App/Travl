package com.travl.guide.mvp.presenter.articles;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.Article;
import com.travl.guide.mvp.model.api.articles.Articles;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.presenter.articles.list.TravlZineArticlesListPresenter;
import com.travl.guide.mvp.view.articles.TravlZineArticlesView;
import com.travl.guide.mvp.view.articles.list.TravlZineArticlesItemView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class TravlZineArticlesPresenter extends MvpPresenter<TravlZineArticlesView> {

    public TravlZineArticlesListPresenter travlZineArticlesListPresenter;
    @Inject
    Router router;
    @Inject
    ArticlesRepo repo;
    @Inject
    @Named("baseUrl")
    String baseUrl;
    private Scheduler scheduler;

    public TravlZineArticlesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        travlZineArticlesListPresenter = new TravlZineArticlesListPresenterImpl();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void loadArticles() {
        Timber.d("Loading articles");
        SingleObserver<Articles> articlesSingleObserver = new SingleObserver<Articles>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Articles articles) {
                travlZineArticlesListPresenter.setArticleList(articles.getArticleList());
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
        repo.getTravlZineArticles().observeOn(scheduler).subscribe(articlesSingleObserver);
    }

    public class TravlZineArticlesListPresenterImpl implements TravlZineArticlesListPresenter {
        PublishSubject<TravlZineArticlesItemView> clickSubject = PublishSubject.create();
        private List<Article> articleList;

        @Override
        public PublishSubject<TravlZineArticlesItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(TravlZineArticlesItemView view) {
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