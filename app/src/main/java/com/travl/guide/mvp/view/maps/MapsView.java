package com.travl.guide.mvp.view.maps;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.mapbox.geojson.Feature;

import java.util.List;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MapsView extends MvpView {
    void setupMapBox();

    void onPlacesLoaded(List<Feature> markerCoordinates);

    @StateStrategyType(value = SkipStrategy.class)
    void showUserLocation();

    void setupFab();
}