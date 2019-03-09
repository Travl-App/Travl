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

    public void initEvents() {
        getViewState().initEvents();
    }

    public void toMapScreen() {
        getViewState().toMapScreen();
    }

    public void initUI() {
        getViewState().initUI();
    }

    public void initStartPageScreen() {
        getViewState().initStartPageScreen();
    }
}
