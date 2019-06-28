package com.travl.guide.mvp.presenter.info;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.view.info.InfoCityView;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@InjectViewState
public class InfoCityPresenter extends MvpPresenter<InfoCityView> {

	@Inject	CityRepo cityRepo;
	@Inject @Named("baseUrl") String baseUrl;

	private int cityId;
	private Disposable disposable;

	public InfoCityPresenter(int cityId) {
		this.cityId = cityId;
	}

	public void loadCity(){
		disposable = cityRepo.loadCity(cityId)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(cityContent -> {
				City city = cityContent.getCity();
				getViewState().showTitle(city.getTitle());
				getViewState().showImageCity(baseUrl + city.getImage());
				getViewState().showDescriptionCity(city.getDescription());
			},
				e -> Timber.e("loadCity() %s", e.getMessage()));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (disposable != null) disposable.dispose();
	}
}
