package com.travl.guide.mvp.view.start.page;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface StartPageView extends MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initArticlesFragment();

    void setCityName(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initCityArticlesFragment();
}
