package com.travl.guide.mvp.view.start.page;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;

import java.util.ArrayList;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface StartPageView extends MvpView {


    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityObjectList(CitiesList cityObjectList);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityContentByCoordinates(CityContent cityContent);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityContentByLinkId(CityContent cityContent);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCurrentCity(CityContent cityContent);

    void requestLocationPermissions();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityStringNames(ArrayList<String> citiesListToCitiesNameList);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void onSpinnerItemClick(String selectedCity);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void addToCityList(City city, boolean isUserCity);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setSpinnerPositionSelected(int position);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void placeSelectedCityOnTop(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void removePlaceIfIsAdded(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityArticles();

    void onLocationPermissionRequestGranted();

    void editPreviousUserCityName(String placeName);

    void transformCityObjectsToCityStrings();

    void addNamesToCitySpinner();
}
