package com.travl.guide.mvp.presenter.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.main.MainView;

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

    public void toTravlZineScreen() {
        getViewState().toTravlZineScreen();
    }

    public void toFavoriteScreen() {
        getViewState().toFavoriteScreens();
    }

    public void toStartPageScreen() {
        getViewState().toStartPageScreen();
    }

    public void onMoveToMapScreen() {
        getViewState().onMoveToMapScreen();
    }

    public void onMoveToTravlZineScreen() {
        getViewState().onMoveToTravlZineScreen();
    }

    public void onMoveToFavoriteScreen() {
        getViewState().onMoveToFavoriteScreen();
    }

    public void onMoveToStartPageScreen() {
        getViewState().onMoveToStartPageScreen();
    }

    public void onMoveToPlaceScreen() {
        getViewState().onMoveToPlaceScreen();
    }

    public void onMoveToArticleScreen() {
        getViewState().onMoveToArticleScreen();
    }

	public void onMoveToInfoCityScreen() {
		getViewState().onMoveToInfoCityScreen();
	}
}
