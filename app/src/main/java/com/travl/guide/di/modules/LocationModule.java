package com.travl.guide.di.modules;

import com.travl.guide.mvp.model.location.LocationRequester;
import com.travl.guide.mvp.model.location.StartPageLocationRequester;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {
    @Provides
    public LocationRequester getLocationRequester() {
        return new StartPageLocationRequester();
    }
}
