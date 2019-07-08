package com.travl.guide.mvp.view.articles;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ArticleView extends MvpView {

    @StateStrategyType(value = SkipStrategy.class)
    void setImageCover(String coverUrl);

    void setAuthorImage(String authorImageUrl);

    void setCategory(String category);

    @StateStrategyType(value = SkipStrategy.class)
    void setTitle(String title);

    @StateStrategyType(value = SkipStrategy.class)
    void setSubTitle(String subtitle);

    void setDate(String date);

    @StateStrategyType(value = SkipStrategy.class)
    void setDescription(String description);

    void setArticlePlaceCardView(String articlePlaceTitle, List<String> placeImageUrls, int placeId);

    @StateStrategyType(value = SkipStrategy.class)
    void setArticlePlaceDescription(String articleText);

}
