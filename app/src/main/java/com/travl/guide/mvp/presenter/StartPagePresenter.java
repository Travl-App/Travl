package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.StartPageView;

import io.reactivex.Scheduler;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> {

    private Scheduler scheduler;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void initPlacesFragment() {
        getViewState().initPlacesFragment();
    }
}
