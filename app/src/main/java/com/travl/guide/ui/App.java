package com.travl.guide.ui;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;
import com.travl.guide.R;
import com.travl.guide.di.interfaces.AppComponent;
import com.travl.guide.di.interfaces.DaggerAppComponent;
import com.travl.guide.di.modules.AppModule;

import org.jetbrains.annotations.Contract;

import timber.log.Timber;

//Created by Pereved on 21.02.2019.
public class App extends Application {

    private static App instance;
    private AppComponent appComponent;

    @Contract(pure = true)
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        Mapbox.getInstance(instance, getString(R.string.mapbox_access_token));
        Timber.plant(new Timber.DebugTree());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}