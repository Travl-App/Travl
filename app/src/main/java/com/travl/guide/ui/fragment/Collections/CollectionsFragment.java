package com.travl.guide.ui.fragment.Collections;

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
import com.travl.guide.mvp.presenter.CollectionsPresenter;
import com.travl.guide.mvp.view.CollectionsView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.adapter.CollectionsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

//Created by Pereved on 21.02.2019.
public class CollectionsFragment extends MvpAppCompatFragment implements CollectionsView {

    private static CollectionsFragment fragment;
    @InjectPresenter
    CollectionsPresenter presenter;
    @BindView(R.id.collection_recycler)
    RecyclerView recycler;

    public static CollectionsFragment getInstance() {
        if (fragment == null)
            fragment = new CollectionsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collections_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupRecycler();
        return view;
    }

    private void setupRecycler() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        CollectionsAdapter adapter = new CollectionsAdapter(presenter.collectionListPresenter);
        recycler.setAdapter(adapter);
    }

    @ProvidePresenter
    public CollectionsPresenter providePresenter() {
        CollectionsPresenter presenter = new CollectionsPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }
}