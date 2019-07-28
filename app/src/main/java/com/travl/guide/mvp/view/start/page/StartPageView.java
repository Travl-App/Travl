package com.travl.guide.mvp.view.start.page;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.location.LocationReceiver;

import java.util.List;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface StartPageView extends MvpView, LocationReceiver {

    void requestLocationPermissions();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setSpinnerPositionSelected(int position);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void placeSelectedCityOnTop(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void removePlaceIfIsAdded(String placeName);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void setCityArticles(City currentCity);

    void onLocationPermissionRequestGranted();

    void addNamesToCitySpinner(List<String> names);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initCityArticlesFragment();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void initTravlZineFragment();

    void initCitySpinner();

    void initMoveToNavigator();
}
