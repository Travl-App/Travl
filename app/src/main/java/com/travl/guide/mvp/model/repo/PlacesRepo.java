package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.api.places.PlaceLink;
import com.travl.guide.mvp.model.api.places.PlacesMap;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.network.NetService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PlacesRepo {
    private NetService netService;

    public PlacesRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<PlacesMap> loadPlacesLinks(String userName, CoordinatesRequest position, double radius) {
        Timber.d("Loading Places");
        return netService.getPlaces(userName, position, radius).subscribeOn(Schedulers.io());
    }

    public Single<Place> loadPlace(String userName, int id) {
        return netService.getPlace(userName, id).subscribeOn(Schedulers.io()).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Timber.e("LoadPlaceError");
                Timber.e(throwable);
            }
        });
    }

    public Single<List<Place>> getPlacesList(String userName, PlacesMap placesMap) {
        Timber.e("getPlacesList");
        List<PlaceLink> placeLinks = placesMap.getPlaces();
        List<Single<Place>> singleList = new ArrayList<>();
        for (int i = 0; i < placeLinks.size(); i++) {
            singleList.add(loadPlace(userName, placeLinks.get(i).getId()));
        }
        return Single.zip(singleList, singles -> {
            List<Place> placeList = new ArrayList<>();
            for (int i = 0; i < singles.length; i++) {
                placeList.add((Place) singles[i]);
            }
            return placeList;
        }).subscribeOn(Schedulers.io());
    }

//    public Single<PlaceEntity> loadPlace(String somethingParameter) {
//        Timber.d("Loading Place");
//        return netService.getPlace(...).subscribeOn(Schedulers.io());
//    }
}
