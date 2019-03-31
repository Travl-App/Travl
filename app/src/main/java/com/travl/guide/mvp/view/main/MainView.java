package com.travl.guide.mvp.view.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndStrategy.class)
public interface MainView extends MvpView {
    void initEvents();

    void initUI();

    @StateStrategyType(value = SkipStrategy.class)
    void toMapScreen();

    @StateStrategyType(value = SkipStrategy.class)
    void toPlaceScreen();

    @StateStrategyType(value = SkipStrategy.class)
    void toStartPageScreen();

    @StateStrategyType(value = SkipStrategy.class)
    void toFavoriteScreen();

    void onMoveToPlaceScreen();

    void onMoveToMapScreen();

    void onMoveToStartPageScreen();

    void onMoveToPostScreen();

    void onMoveToFavoriteScreen();
}
