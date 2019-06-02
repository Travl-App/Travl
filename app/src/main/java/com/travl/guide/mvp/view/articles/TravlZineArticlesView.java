package com.travl.guide.mvp.view.articles;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface TravlZineArticlesView extends MvpView {

    void onChangedArticlesData();

    void onNoMoreArticles();

    void showContainer();
}