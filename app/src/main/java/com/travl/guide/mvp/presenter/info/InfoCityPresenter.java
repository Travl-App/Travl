package com.travl.guide.mvp.presenter.info;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.info.InfoCityView;

@InjectViewState
public class InfoCityPresenter extends MvpPresenter<InfoCityView> {

	private int cityId;

	public InfoCityPresenter(int cityId) {
		this.cityId = cityId;
	}
}
