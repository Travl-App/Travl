package com.travl.guide.ui.fragment.articles.city;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.articles.CityArticlesPresenter;
import com.travl.guide.mvp.view.articles.CityArticlesView;
import com.travl.guide.ui.App;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CityArticlesFragment extends MvpAppCompatFragment implements CityArticlesView {

    @InjectPresenter
    CityArticlesPresenter presenter;

    @ProvidePresenter
    public CityArticlesPresenter providePresenter() {
        CityArticlesPresenter presenter = new CityArticlesPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.articles_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupRecycler();
        presenter.loadArticles();
        return view;
    }

    private void setupRecycler() {

    }

    @Override
    public void onChangedArticlesData() {

    }
}
