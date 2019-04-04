package com.travl.guide.mvp.presenter.favourite;

import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.favorite.FavoriteModel;
import com.travl.guide.mvp.view.favourite.FavoriteView;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;

//Created by Pereved on 29.03.2019.
public class FavoritePresenter extends MvpPresenter<FavoriteView> {

    @Inject
    Router router;
    private FavoriteModel model;
    private Scheduler scheduler;

    public FavoritePresenter(){}

    public FavoritePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        if(model == null) this.model = new FavoriteModel();
    }
}
