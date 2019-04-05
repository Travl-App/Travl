package com.travl.guide.ui.fragment.articles;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.articles.ArticlePresenter;
import com.travl.guide.mvp.view.articles.ArticleView;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

public class ArticleFragment extends MvpAppCompatFragment implements ArticleView {

    private static final String ARTICLE_ID_KEY = "article id key";

    @BindView(R.id.article_toolbar)
    Toolbar toolbar;
    @BindView(R.id.article_web_view)
    WebView articleWebVew;

    @Inject
    Router router;

    @InjectPresenter
    ArticlePresenter presenter;

    public static ArticleFragment getInstance(String articleUrl) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID_KEY, articleUrl);
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @ProvidePresenter
    public ArticlePresenter providePresenter() {
        String articleUrl = getArguments().getString(ARTICLE_ID_KEY);
        return new ArticlePresenter(articleUrl);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupWebView();
        setupToolbar();
        return view;
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void setupWebView() {
        articleWebVew.getSettings().setJavaScriptEnabled(true);
        articleWebVew.getSettings().setDomStorageEnabled(true);
        articleWebVew.setWebViewClient(new MyWebViewClient());
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void loadWebView(String url) {
        articleWebVew.loadUrl(url);
    }

    public void onBackPressed() {
        if (articleWebVew.canGoBack()) {
            articleWebVew.goBack();
        } else if (getActivity() != null) getActivity().onBackPressed();
        else throw new RuntimeException("Activity is null");
    }

    private class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.contains("https://travl.dev/api/places")) {
                router.navigateTo(new Screens.PlaceScreen(Integer.parseInt(url.substring(0, url.length() - 1).substring(29))));
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Timber.e("Webview " + error.toString());
            super.onReceivedError(view, request, error);
        }

    }

}
