package com.travl.guide.ui.fragment.articles.city;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.CityArticlesPresenter;
import com.travl.guide.mvp.view.articles.CityArticlesView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.adapter.articles.city.CityArticlesAdapter;
import com.travl.guide.ui.fragment.start.page.StartPageFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class CityArticlesFragment extends MvpAppCompatFragment implements CityArticlesView, StartPageFragment.ArticlesReceiver {

    @BindView(R.id.city_articles_preview_recycler)
    RecyclerView cityArticlesPreviewRecycler;
    @Inject
    IImageLoader imageLoader;
    @InjectPresenter
    CityArticlesPresenter presenter;
    private int articlesNumber;


    @ProvidePresenter
    public CityArticlesPresenter providePresenter() {
        CityArticlesPresenter presenter = new CityArticlesPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_articles_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupRecycler();
        return view;
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cityArticlesPreviewRecycler.setLayoutManager(linearLayoutManager);
        CityArticlesAdapter adapter = new CityArticlesAdapter(presenter.getCityArticlesListPresenter(), imageLoader);
        cityArticlesPreviewRecycler.setAdapter(adapter);
    }

    @Override
    public void onChangedArticlesData() {
        if (cityArticlesPreviewRecycler != null && cityArticlesPreviewRecycler.getAdapter() != null) {
            cityArticlesPreviewRecycler.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showContainer() {
        Timber.e("showing.........");
        setContainerVisibility(View.VISIBLE);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        hideContainer();
    }

    @Override
    public void hideContainer() {
        Timber.e("hiding.........");
        setContainerVisibility(View.GONE);
    }


    private void setContainerVisibility(int visibility) {
        Activity activity = getActivity();
        if (activity != null) {
            FrameLayout container = activity.findViewById(R.id.start_page_city_articles_container);
            TextView title = activity.findViewById(R.id.start_page_city_articles_container_title);
            if (container != null) {
                container.setVisibility(visibility);
                if (title != null) {
                    title.setVisibility(visibility);
                    Timber.e("All set to " + visibility);
                }
            }
        }
    }

    @Override
    public void setArticles(List<ArticleLink> articleLinks) {
        if (presenter != null) {
            presenter.getCityArticlesListPresenter().setArticleLinkList(articleLinks);
            articlesNumber = articleLinks.size();
        }
    }

    @Override
    public int getArticlesNumber() {
        return articlesNumber;
    }

}
