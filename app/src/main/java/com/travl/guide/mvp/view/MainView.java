package com.travl.guide.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndStrategy.class)
public interface MainView extends MvpView {
    void initEvents();

    void initUI();

    void toMapScreen();

    void toPlaceScreen();

    void toStartPageScreen();
}
