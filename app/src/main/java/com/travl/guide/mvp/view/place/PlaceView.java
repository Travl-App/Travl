package com.travl.guide.mvp.view.place;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PlaceView extends MvpView {
    void setTitleTextView(String title);

    void setSubtitleTextView(String subtitle);

    void setImageSlider(String imageUrl);

    void setPlaceAddressTextView(String placeAddress);

    void setPlaceAuthorNameTextView(String authorName);

    void setTextView(String text);
}
