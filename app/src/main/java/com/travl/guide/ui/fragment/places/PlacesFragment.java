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
import com.travl.guide.mvp.presenter.PlacesPresenter;
import com.travl.guide.mvp.view.PlacesView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.adapter.PlacesAdapter;

import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

//Created by Pereved on 21.02.2019.
public class PlacesFragment extends MvpAppCompatFragment implements PlacesView {

    private static PlacesFragment fragment = new PlacesFragment();
    @InjectPresenter
    PlacesPresenter presenter;

    @Singleton
    public static PlacesFragment getInstance() {
        return fragment;
    }
    @BindView(R.id.collection_recycler)
    RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.places_fragment, container, false);
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

        PlacesAdapter adapter = new PlacesAdapter(presenter.placePresenter);
        recycler.setAdapter(adapter);
    }

    @ProvidePresenter
    public PlacesPresenter providePresenter() {
        PlacesPresenter presenter = new PlacesPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }
}