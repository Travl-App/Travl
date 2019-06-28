package com.travl.guide.mvp.presenter.info;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.info.InfoCity;
import com.travl.guide.mvp.model.database.RoomHelper;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.view.info.InfoCityView;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@InjectViewState
public class InfoCityPresenter extends MvpPresenter<InfoCityView> {

	@Inject	RoomHelper roomHelper;
	@Inject	CityRepo cityRepo;
	@Inject @Named("baseUrl") String baseUrl;

	private int cityId;
	private Disposable disposable;

	public InfoCityPresenter(int cityId) {
		this.cityId = cityId;
	}

	public void loadCity() {
		checkDataBase(cityId);
	}

	private void loadFromServer(int cityId){
		disposable = cityRepo.loadCity(cityId)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(cityContent -> {
					City city = cityContent.getCity();
					InfoCity infoCity = greatInfoCity(city);
					showInfo(infoCity);
					insetToDataBase(infoCity);
				},
				e -> Timber.e("loadCity() %s", e.getMessage()));

	}

	private InfoCity greatInfoCity(City city){
		InfoCity info = new InfoCity();
		info.setIdCity(city.getIdCity());
		info.setTitle(city.getTitle());
		info.setDescription(city.getDescription());
		info.setImage(city.getImage());
		return info;
	}

	private void insetToDataBase(InfoCity city){
		disposable = roomHelper.insertCityToDataBase(city)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(aLong -> Timber.i("%d add into base", aLong),
				e -> Timber.e("error added to base %s", e.getMessage()));
	}

	private void checkDataBase(int cityId){
		disposable = roomHelper.getCityFromDataBase(cityId)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::showInfo,
				e -> Timber.e("err database %s", e.getMessage()),
				() -> loadFromServer(cityId));
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
