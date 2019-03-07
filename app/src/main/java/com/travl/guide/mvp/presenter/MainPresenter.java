package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.MainView;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.Screen;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    @Inject
    Router router;
    private Scheduler scheduler;

    public MainPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void changingScreen() {
        getViewState().replaceScreen();
    }

    public void showCurrentFragment() {
        getViewState().showCurrentFragment();
    }

    public void replaceScreen(Screen screen) {
        router.replaceScreen(screen);
    }

    public void initPlacesScreen() {
        getViewState().initPlacesScreen();
    }

    public void initMapScreen() {
        getViewState().initMapScreen();
    }
}
