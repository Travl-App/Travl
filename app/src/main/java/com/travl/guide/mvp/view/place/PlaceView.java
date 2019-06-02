package com.travl.guide.mvp.view.place;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PlaceView extends MvpView {
    void setPlaceCategory(String placeCategory);

    void setPlaceTitle(String placeTitle);

//    void setPlaceSubtitle(String placeSubtitle);

    void setPlaceImages(List<String> placeImageUrls);

    void setPlaceAddress(String placeAddress);

    void setPlaceRoute(String placeRoute);

    void setPlacePopularity(int placePopularity);

    void setPlaceDescription(String placeDescription);

    void setPlaceAuthorName(String placeAuthorName);

    void setPlaceCoordinates(double[] placeCoordinates);
}
