package com.travl.guide.mvp.view.start.page;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;

import java.util.ArrayList;
import java.util.List;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface StartPageView extends MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initTravlZineFragment();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initCityArticlesFragment();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityObjectList(CitiesList cityObjectList);

    @StateStrategyType(value = SkipStrategy.class)
    void setCityContentByCoordinates(CityContent cityContent);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityContentByLinkId(CityContent cityContent);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCity(CityContent cityContent);

    void requestCoordinates();

    void requestLocation();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityStringNames(ArrayList<String> citiesListToCitiesNameList);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onSpinnerItemClick(String selectedCity);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void addToCityList(City city);

    void initCitySpinner();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCitySelectedName(String citySelected);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setSpinnerPositionSelected(int position);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void placeSelectedCityOnTop(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void removePlaceIfIsAdded(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void addNamesToCitySpinner(List<String> cityStringNames);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityArrayAdapter();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void addCityArrayAdapterToSpinner();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityArticles();
}
