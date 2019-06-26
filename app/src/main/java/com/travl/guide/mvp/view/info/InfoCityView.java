package com.travl.guide.mvp.view.info;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface InfoCityView extends MvpView {
	@StateStrategyType(AddToEndSingleStrategy.class)
	void showTitle(String title);
	@StateStrategyType(AddToEndSingleStrategy.class)
	void showImageCity(String url);
	@StateStrategyType(AddToEndSingleStrategy.class)
	void showDescriptionCity(String description);
}
