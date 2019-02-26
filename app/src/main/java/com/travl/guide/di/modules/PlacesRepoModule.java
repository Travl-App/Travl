package com.travl.guide.di.modules;

import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.mvp.model.repo.PlacesRepo;

import dagger.Module;
import dagger.Provides;

@Module
public class PlacesRepoModule {
    @Provides
    public PlacesRepo getPlacesRepo(NetService netService) {
        return new PlacesRepo(netService);
    }
}
