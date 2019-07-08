package com.travl.guide.mvp.presenter.articles;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.Article;
import com.travl.guide.mvp.model.api.articles.ArticlePlace;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.view.articles.ArticleView;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<ArticleView> {

    @Inject
    Router router;

    @Inject
    ArticlesRepo articlesRepo;

    @Inject
    @Named("baseUrl")
    String baseUrl;

    private Disposable disposable;
    private Scheduler scheduler;

    public ArticlePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);
    }

    public void loadArticle(int id) {
        disposable = articlesRepo.getArticle(id).observeOn(scheduler).subscribe(articleContainer -> {
            int status = articleContainer.getStatus();
            if (status == 200) {
                Article article = articleContainer.getArticle();
                if (article != null) {
                    String coverUrl = article.getCoverUrl();
                    if (coverUrl != null) {
                        getViewState().setImageCover(baseUrl + coverUrl.substring(1));
                    }

                    getViewState().setCategory(article.getCategories().get(0).getName());
                    getViewState().setTitle(article.getTitle());
                    getViewState().setSubTitle(article.getSubtitle());
                    getViewState().setDate(article.getModified());
                    getViewState().setDescription(article.getDescription());
                    List<ArticlePlace> articlePlaceList = article.getArticlePlaces();
                    for (ArticlePlace articlePlace : articlePlaceList) {
                        String placeCoverUrl = articlePlace.getPlaceImageUrl();
                        if (placeCoverUrl != null) {
                            getViewState().setArticlePlaceCardView(articlePlace.getTitle(), articlePlace.getOther_images(), articlePlace.getId());
                        }
                        getViewState().setArticlePlaceDescription(articlePlace.getArticleText());
                    }

                }
            }
        }, Timber::e);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
    }

    public void showPlace(int placeId) {
        router.navigateTo(new Screens.PlaceScreen(placeId));
    }
}
