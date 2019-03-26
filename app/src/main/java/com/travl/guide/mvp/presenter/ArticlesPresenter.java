package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.Article;
import com.travl.guide.mvp.model.api.articles.Articles;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.presenter.list.ArticleListPresenter;
import com.travl.guide.mvp.view.ArticlesView;
import com.travl.guide.mvp.view.list.ArticlesItemView;

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
public class ArticlesPresenter extends MvpPresenter<ArticlesView> {

    @Inject
    Router router;
    private Scheduler scheduler;
    public ArticleListPresenter articleListPresenter;
    @Inject
    ArticlesRepo repo;
    @Inject
    @Named("baseUrl")
    String baseUrl;

    public ArticlesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        articleListPresenter = new ArticleListPresenterImpl();
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
                articleListPresenter.setArticleList(articles.getArticleList());
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
        repo.getArticles(User.getInstance().getDefaultUserName()).observeOn(scheduler).subscribe(articlesSingleObserver);
    }

    public class ArticleListPresenterImpl implements ArticleListPresenter {
        PublishSubject<ArticlesItemView> clickSubject = PublishSubject.create();
        private List<Article> articleList;

        @Override
        public PublishSubject<ArticlesItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(ArticlesItemView view) {
            Timber.d("BindView and set Description");
            Article article = articleList.get(view.getPos());
            view.setDescription(article.getTitle());
        }

        @Override
        public int getListCount() {
            Timber.d("PlaceList size = %s", (articleList == null ? null : articleList.size()));
            return articleList == null ? 0 : articleList.size();
        }

        @Override
        public void setArticleList(List<Article> articles) {
            this.articleList = articles;
            getViewState().onChangedArticlesData();
        }
    }
}