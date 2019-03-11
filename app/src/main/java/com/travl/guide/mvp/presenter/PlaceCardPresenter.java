package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.PlaceCardView;

import io.reactivex.Scheduler;

@InjectViewState
public class PlaceCardPresenter extends MvpPresenter<PlaceCardView> {

    private Scheduler scheduler;

    public PlaceCardPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
