package com.travl.guide.ui.fragment.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.favourite.FavoritePresenter;
import com.travl.guide.mvp.view.favourite.FavoriteView;
import com.travl.guide.navigator.CurrentScreen;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.OnMoveToNavigator;

//Created by Pereved on 29.03.2019.
public class FavoriteFragment extends MvpAppCompatFragment implements FavoriteView {

    @InjectPresenter
    FavoritePresenter presenter;
    private OnMoveToNavigator moveToNavigator;

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (moveToNavigator != null) {
            moveToNavigator.onMoveTo(CurrentScreen.INSTANCE.favorite());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoveToNavigator) {
            moveToNavigator = (OnMoveToNavigator) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        return view;
    }
}
