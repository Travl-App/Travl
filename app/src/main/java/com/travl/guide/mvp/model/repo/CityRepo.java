package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.ui.utils.NetworkStatus;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CityRepo {

    private NetService netService;

    public CityRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<CityContent> getCityContent(CoordinatesRequest position) {
        if (NetworkStatus.isOnline()) {
            return netService.loadCityContent(position).subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                Timber.e(throwable);
                return null;
            });
        } else {
            return new Single<CityContent>() {
                @Override
                protected void subscribeActual(SingleObserver<? super CityContent> observer) {
                }
            };
        }
    }

    public Single<CitiesList> getCitiesList() {
        if (NetworkStatus.isOnline()) {
            return netService.loadCitiesList().subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                Timber.e(throwable);
                return null;
            });
        } else {
            return new Single<CitiesList>() {
                @Override
                protected void subscribeActual(SingleObserver<? super CitiesList> observer) {
                }
            };
        }
    }

    public Single<CityContent> loadCity(int id) {
        if (NetworkStatus.isOnline()) {
            return netService.loadCity(id).subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                Timber.e(throwable);
                return null;
            });
        } else {
            return new Single<CityContent>() {
                @Override
                protected void subscribeActual(SingleObserver<? super CityContent> observer) {
                }
            };
        }
    }

    private String cityName;

    public void saveCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
