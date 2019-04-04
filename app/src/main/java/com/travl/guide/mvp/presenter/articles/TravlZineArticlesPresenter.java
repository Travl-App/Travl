package com.travl.guide.mvp.presenter.articles;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.presenter.articles.list.TravlZineArticlesListPresenter;
import com.travl.guide.mvp.view.articles.TravlZineArticlesView;
import com.travl.guide.mvp.view.articles.list.TravlZineArticlesItemView;
import com.travl.guide.navigator.Screens;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
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

    @SuppressLint("CheckResult")
    public void loadArticles() {
        Timber.d("Loading articles");
        repo.getTravlZineArticles().observeOn(scheduler).subscribe(articles -> travlZineArticlesListPresenter.setArticleLinkList(articles.getArticleLinkList()), Timber::e);
    }

    public class TravlZineArticlesListPresenterImpl implements TravlZineArticlesListPresenter {
        PublishSubject<TravlZineArticlesItemView> clickSubject = PublishSubject.create();
        private List<ArticleLink> articleLinkList;

        @Override
        public PublishSubject<TravlZineArticlesItemView> getClickSubject() {
            return clickSubject;
        }

        @SuppressLint("CheckResult")
        @Override
        public void bindView(TravlZineArticlesItemView view) {
            Timber.d("BindView and set Description");
            ArticleLink articleLink = articleLinkList.get(view.getPos());
            clickSubject.subscribe(travlZineArticlesItemView -> router.navigateTo(new Screens.ArticleScreen(articleLink.getLink())));
            String title = articleLink.getTitle();
            String imageUrl = articleLink.getImageCoverUrl();
            if (title != null) view.setDescription(title);
            if (imageUrl != null)
                view.setImage(baseUrl + imageUrl.substring(1));
        }

        @Override
        public int getListCount() {
            Timber.d("PlaceList size = %s", (articleLinkList == null ? null : articleLinkList.size()));
            return articleLinkList == null ? 0 : articleLinkList.size();
        }

        @Override
        public void setArticleLinkList(List<ArticleLink> articleLinks) {
            this.articleLinkList = articleLinks;
            if (articleLinkList == null || articleLinkList.size() == 0) {
                articleLinkList = new ArrayList<>();
                articleLinkList.add(new ArticleLink("Проверьте соединение", null));
            }
            getViewState().onChangedArticlesData();
        }
    }
}