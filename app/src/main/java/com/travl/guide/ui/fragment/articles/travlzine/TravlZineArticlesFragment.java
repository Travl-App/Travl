package com.travl.guide.ui.fragment.articles.travlzine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.TravlZineArticlesPresenter;
import com.travl.guide.mvp.view.articles.TravlZineArticlesView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.adapter.articles.travlzine.TravlZineArticlesAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TravlZineArticlesFragment extends MvpAppCompatFragment implements TravlZineArticlesView {

    private static final int SPAN_COUNT = 2;
    @InjectPresenter
    TravlZineArticlesPresenter presenter;
    @Inject
    IImageLoader imageLoader;
    @BindView(R.id.travlzine_articles_preview_recycler)
    RecyclerView travzineArticlesPreviewRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.travlzine_articles_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupRecycler();
        presenter.loadArticles();
        return view;
    }

    private void setupRecycler() {

        travzineArticlesPreviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        TravlZineArticlesAdapter adapter = new TravlZineArticlesAdapter(presenter.travlZineArticlesListPresenter, imageLoader);
        travzineArticlesPreviewRecycler.setAdapter(adapter);
    }

    @ProvidePresenter
    public TravlZineArticlesPresenter providePresenter() {
        TravlZineArticlesPresenter presenter = new TravlZineArticlesPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    public void onChangedArticlesData() {
        if (travzineArticlesPreviewRecycler != null && travzineArticlesPreviewRecycler.getAdapter() != null) {
            travzineArticlesPreviewRecycler.getAdapter().notifyDataSetChanged();
        }
    }
}