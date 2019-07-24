package com.travl.guide.di.modules;

import com.travl.guide.mvp.model.database.RoomCache;
import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.model.repo.InfoCityRepo;

import dagger.Module;
import dagger.Provides;

@Module
public class CityRepoModule {

	@Provides
	InfoCityRepo provideInfoCityRepo(RoomCache roomCache, NetService netService){
		return new InfoCityRepo(roomCache, netService);
	}

	@Provides
	CityRepo getPlacesRepo(NetService netService) {
        return new CityRepo(netService);
    }
}
