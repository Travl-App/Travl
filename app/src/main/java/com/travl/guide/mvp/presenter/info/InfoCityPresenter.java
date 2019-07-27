package com.travl.guide.mvp.presenter.info;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.database.models.InfoCity;
import com.travl.guide.mvp.model.repo.InfoCityRepo;
import com.travl.guide.mvp.view.info.InfoCityView;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@InjectViewState
public class InfoCityPresenter extends MvpPresenter<InfoCityView> {

	@Inject	InfoCityRepo infoCityRepo;
	@Inject @Named("baseUrl") String baseUrl;

	private int cityId;
	private Disposable disposable;

	public InfoCityPresenter(int cityId) {
		this.cityId = cityId;
	}

	public void loadCity() {
		disposable =  infoCityRepo.loadInfoCity(cityId)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(this::showInfo,
			e -> Timber.e("loadCity() %s", e.getMessage()));
	}

	private void showInfo(InfoCity city){
		getViewState().showTitle(city.getTitle());
		getViewState().showImageCity(baseUrl + city.getImage());
		getViewState().showDescriptionCity(city.getDescription());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (disposable != null) disposable.dispose();
	}
}
