package com.travl.guide.mvp.view.maps;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.mapbox.geojson.Feature;
import com.travl.guide.mvp.model.api.places.Place;

import java.util.List;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface MapsView extends MvpView {
    void setupMapBox();

    void onPlacesLoaded(List<Feature> markerCoordinates);

    void onRequestCompleted(List<Place> viewMap);

    @StateStrategyType(value = SkipStrategy.class)
    void findUser();
}