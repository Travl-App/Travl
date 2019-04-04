package com.travl.guide.di.modules;

import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.mvp.model.repo.CityRepo;

import dagger.Module;
import dagger.Provides;

@Module
public class CityRepoModule {
    @Provides
    public CityRepo getPlacesRepo(NetService netService) {
        return new CityRepo(netService);
    }
}
