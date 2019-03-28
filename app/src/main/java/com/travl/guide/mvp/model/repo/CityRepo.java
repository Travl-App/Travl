package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.network.NetService;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CityRepo {

    private NetService netService;

    public CityRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<CityContent> getCityContent(CoordinatesRequest position) {
        return netService.getCityContent(position).subscribeOn(Schedulers.io());
    }
}
