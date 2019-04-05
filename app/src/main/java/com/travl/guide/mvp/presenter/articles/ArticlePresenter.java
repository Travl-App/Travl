package com.travl.guide.mvp.presenter.articles;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.articles.ArticleView;
import com.travl.guide.ui.App;

import javax.inject.Inject;
import javax.inject.Named;

import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<ArticleView> {

    @Inject
    Router router;

    @Inject
    @Named("baseUrl")
    String baseUrl;

    private String articleUrl;

    public ArticlePresenter() {
        App.getInstance().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public void loadUrl() {
        Timber.e("Url = " + articleUrl.substring(5));
        getViewState().loadWebView(baseUrl + articleUrl.substring(5));
    }
}
