package com.travl.guide.mvp.view.articles;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface CityArticlesView extends MvpView {
    void onChangedArticlesData();

    void showContainer();

    void hideContainer();
}
