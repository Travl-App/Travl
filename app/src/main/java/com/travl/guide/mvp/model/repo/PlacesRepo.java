package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.mvp.model.places.PlacesMap;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class PlacesRepo {
    private NetService netService;

    public PlacesRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<PlacesMap> loadPlaces(String userName, String position, double radius) {
        return Single.create((SingleOnSubscribe<PlacesMap>) emitter -> {
            PlacesMap placesMap = netService.getPlaces(userName, position, radius);
            if (placesMap != null) {
                emitter.onSuccess(placesMap);
            } else {
                emitter.onError(new NullPointerException("Cards Load failed."));
            }
        }).subscribeOn(Schedulers.io());
    }
}
