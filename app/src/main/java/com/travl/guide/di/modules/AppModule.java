package com.travl.guide.di.modules;

import com.travl.guide.ui.App;

import dagger.Module;
import dagger.Provides;

//Created by Pereved on 23.02.2019.
@Module
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public App app() {
        return app;
    }
}