package com.travl.guide.mvp.view.place;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PlaceView extends MvpView {
    void setPlaceTitle(String title);

    void setPlaceSubtitle(String subtitle);

    void setPlaceImages(List<String> imageUrls);

    void setPlaceAddress(String placeAddress);

    void setPlaceAuthorName(String authorName);

    void setPlaceCoordinates(double[] coordinates);

    void setPlaceDescription(String description);
}
