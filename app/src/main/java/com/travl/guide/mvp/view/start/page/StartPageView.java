package com.travl.guide.mvp.view.start.page;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.CityContent;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface StartPageView extends MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initArticlesFragment();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initCityArticlesFragment();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCitiesList(CitiesList citiesList);

    void setCityContent(CityContent cityContent);
}
