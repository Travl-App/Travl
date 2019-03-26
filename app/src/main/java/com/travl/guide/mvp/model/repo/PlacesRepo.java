package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.api.places.PlacesMap;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.network.NetService;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PlacesRepo {
    private NetService netService;

    public PlacesRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<PlacesMap> loadPlacesForMap(String userName, CoordinatesRequest position, double radius, int detailed) {
        Timber.d("Loading Places");
        return netService.getPlacesForMap(userName, position, radius, detailed).subscribeOn(Schedulers.io());
    }

    public Single<Place> loadPlace(String userName, int id) {
        return netService.getPlace(userName, id).subscribeOn(Schedulers.io()).doOnError(throwable -> {
            Timber.e("LoadPlaceError");
            Timber.e(throwable);
        });
    }
}
