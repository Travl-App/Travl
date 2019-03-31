package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.places.ManyPlacesContainer;
import com.travl.guide.mvp.model.api.places.PlaceContainer;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.ui.utils.NetworkStatus;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PlacesRepo {

    private NetService netService;

    public PlacesRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<ManyPlacesContainer> loadPlacesForMap(CoordinatesRequest position, double radius, int detailed) {
        Timber.d("Loading Places");
        if (NetworkStatus.isOnline()) {
            return netService.loadPlacesForMap(position, radius, detailed).subscribeOn(Schedulers.io()).doOnError(Timber::e);
        } else {
            return new Single<ManyPlacesContainer>() {
                @Override
                protected void subscribeActual(SingleObserver<? super ManyPlacesContainer> observer) {
                }
            };
        }
    }

    public Single<PlaceContainer> loadPlace(int id) {
        Timber.e("loading place with id = " + id);
        if (NetworkStatus.isOnline()) {
            return netService.loadPlace(id).subscribeOn(Schedulers.io()).doOnError(throwable -> {
                Timber.e("LoadPlaceError");
                Timber.e(throwable);
            });
        } else {
            return new Single<PlaceContainer>() {
                @Override
                protected void subscribeActual(SingleObserver<? super PlaceContainer> observer) {
                }
            };
        }
    }
}
