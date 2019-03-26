package com.travl.guide.ui.fragment.places;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.travl.guide.mvp.presenter.ArticlesPresenter;
import com.travl.guide.mvp.view.ArticlesView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.adapter.ArticlesAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ArticlesFragment extends MvpAppCompatFragment implements ArticlesView {

    private static final int SPAN_COUNT = 2;
    @InjectPresenter
    ArticlesPresenter presenter;
    @Inject
    IImageLoader imageLoader;
    @BindView(R.id.collection_recycler)
    RecyclerView recycler;

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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        }
        ArticlesAdapter adapter = new ArticlesAdapter(presenter.articleListPresenter, imageLoader);
        recycler.setAdapter(adapter);
    }

    @ProvidePresenter
    public ArticlesPresenter providePresenter() {
        ArticlesPresenter presenter = new ArticlesPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    public void onChangedArticlesData() {
        if (recycler != null && recycler.getAdapter() != null) {
            recycler.getAdapter().notifyDataSetChanged();
        }
    }
}