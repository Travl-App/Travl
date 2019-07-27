package com.travl.guide.mvp.view.maps;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.travl.guide.mvp.model.api.places.map.Place;
import com.travl.guide.mvp.model.api.places.map.PlaceLink;

import java.util.List;

@StateStrategyType(AddToEndStrategy.class)
public interface MapsView extends MvpView {
//    void setupMapBox();

    void onPlacesLoaded(List<PlaceLink> places, boolean isLast);

    void onRequestCompleted(List<Place> viewMap);

    @StateStrategyType(value = SkipStrategy.class)
    void showUserLocation();

//    void setupFab();

    void showLoadInfo();

    void hideLoadInfo();
}