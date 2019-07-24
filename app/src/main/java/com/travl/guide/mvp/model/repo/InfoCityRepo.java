package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.database.RoomCache;
import com.travl.guide.mvp.model.database.models.InfoCity;
import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.ui.utils.NetworkStatus;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class InfoCityRepo{

	private RoomCache roomCache;
	private NetService netService;

	public InfoCityRepo(RoomCache roomCache, NetService netService) {
		this.roomCache = roomCache;
		this.netService = netService;
	}

	public Single<InfoCity> loadInfoCity(int cityId) {
		return checkCache(cityId).subscribeOn(Schedulers.io());
	}

	private Single<InfoCity> checkCache(int cityId){
		return roomCache.getInfoCityFromBase(cityId)
			.switchIfEmpty(loadFromServer(cityId));
	}

	//fixMe переделать как только будет готово API InfoCity
	private Single<InfoCity> loadFromServer(int cityId){
		if (NetworkStatus.isOnline()) {
			Single<InfoCity> fromServer = netService.loadCity(cityId)
				.flatMap(cityContent ->  Single.just(cityContent.getCity())
				.flatMap(city -> Single.just(new InfoCityMapping().greatInfoCity(city))))
				.subscribeOn(Schedulers.io());
			Disposable disposable = fromServer.observeOn(Schedulers.computation())
				.subscribe(this::insertInfoCityToCache,
					e -> Timber.e("insertInfoCityToCache %s", e.getMessage()));
			return fromServer;
		}
		Timber.e("loadFromServer no network");
		return Single.error(new RuntimeException("no network"));
	}

	private void insertInfoCityToCache(InfoCity infoCity){
		Disposable disposable = roomCache.insertInfoCityToBase(infoCity)
			.observeOn(Schedulers.computation())
			.subscribe(() -> Timber.i("insertInfoCityToBase done"),
					e -> Timber.e("insertInfoCityToBase %s", e.getMessage()));
	}
}
