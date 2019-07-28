package com.travl.guide.ui.fragment.articles.travlzine;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.TravlZineArticlesPresenter;
import com.travl.guide.mvp.view.articles.TravlZineArticlesView;
import com.travl.guide.navigator.CurrentScreen;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.OnMoveToNavigator;
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
    private OnMoveToNavigator moveToNavigator;

    @Override
    public void showContainer() {
        Activity activity = getActivity();
        if (activity != null) {
            FrameLayout container = activity.findViewById(R.id.start_page_travl_zine_container);
            TextView title = activity.findViewById(R.id.start_page_travl_zine_container_title);
            if (container != null) {
                container.setVisibility(View.VISIBLE);
                if (title != null) {
                    title.setVisibility(View.VISIBLE);
                }
            }
        }
    }

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoveToNavigator) {
            moveToNavigator = (OnMoveToNavigator) context;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (moveToNavigator != null) {
            moveToNavigator.onMoveTo(CurrentScreen.INSTANCE.travlzine());
        }
    }

    private void setupRecycler() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            travzineArticlesPreviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            travzineArticlesPreviewRecycler.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        }

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

    @Override
    public void onNoMoreArticles() {
        Toast.makeText(this.getContext(), getString(R.string.no_more_articles), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDispose();
    }
}