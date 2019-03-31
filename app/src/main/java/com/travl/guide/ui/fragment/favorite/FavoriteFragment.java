package com.travl.guide.ui.fragment.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.favourite.FavoritePresenter;
import com.travl.guide.mvp.view.favourite.FavoriteView;
import com.travl.guide.ui.App;

//Created by Pereved on 29.03.2019.
public class FavoriteFragment extends MvpAppCompatFragment implements FavoriteView {

    @InjectPresenter
    FavoritePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        return view;
    }
}
