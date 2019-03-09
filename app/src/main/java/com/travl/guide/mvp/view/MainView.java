package com.travl.guide.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.terrakok.cicerone.Screen;

@StateStrategyType(value = AddToEndStrategy.class)
public interface MainView extends MvpView {
    void initEvents();

    void toMapScreen();

    void toPlaceScreen();

    void initUI();
}
