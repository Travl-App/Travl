package com.travl.guide.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PlaceCardView extends MvpView {
    void setAuthorNameTextView(String userName);
    void setAuthorAvatarImageView(String imageUrl);
    void setTitleTextView(String title);
    void setFirstTextView(String text);
    void setFirstImageView(String imageUrl);
    void setSecondTextView(String text);
    void setSecondImageView(String imageUrl);
}
