package com.travl.guide.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

//Created by Pereved on 29.03.2019.
@StateStrategyType(value = AddToEndStrategy.class)
public interface FavoriteView extends MvpView {
}