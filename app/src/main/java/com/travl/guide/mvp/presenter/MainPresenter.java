package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public void initEvents() {
        getViewState().initEvents();
    }

    public void initUI() {
        getViewState().initUI();
    }

    public void toMapScreen() {
        getViewState().toMapScreen();
    }

    public void toPlaceScreen() {
        getViewState().toPlaceScreen();
    }

    public void toStartPageScreen() {
        getViewState().toStartPageScreen();
    }

    public void onMoveToMapScreen() {
        getViewState().onMoveToMapScreen();
    }

    public void onMoveToPlaceScreen() {
        getViewState().onMoveToPlaceScreen();
    }

    public void onMoveToStartPageScreen() {
        getViewState().onMoveToStartPageScreen();
    }

    public void onMoveToPostScreen() {
        getViewState().onMoveToPostScreen();
    }
}
