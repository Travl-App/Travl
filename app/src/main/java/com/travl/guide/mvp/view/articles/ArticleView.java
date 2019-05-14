package com.travl.guide.mvp.view.articles;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ArticleView extends MvpView {
    void setTitle(String title);

    void setSubTitle(String subtitle);

    void setDescription(String description);

    void setImageCover(String coverUrl);

    void setArticlePlaceCover(String placeImageUrl, int placeId);

    void setArticlePlaceDescription(String articleText);
}
