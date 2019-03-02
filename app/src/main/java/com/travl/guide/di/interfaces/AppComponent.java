package com.travl.guide.di.interfaces;

import com.travl.guide.di.modules.AppModule;
import com.travl.guide.di.modules.CiceroneModule;
import com.travl.guide.di.modules.ImageLoaderModule;
import com.travl.guide.di.modules.NetModule;
import com.travl.guide.di.modules.PlacesRepoModule;
import com.travl.guide.mvp.presenter.MapsPresenter;
import com.travl.guide.mvp.presenter.PlacesPresenter;
import com.travl.guide.ui.activity.MainActivity;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.places.PlacesFragment;

import javax.inject.Singleton;

import dagger.Component;

//Created by Pereved on 23.02.2019.
@Singleton
@Component(modules = {
        AppModule.class,
        CiceroneModule.class,
        NetModule.class,
        PlacesRepoModule.class,
        ImageLoaderModule.class
})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(MapsFragment mapFragment);

    void inject(MapsPresenter mapPresenter);

    void inject(PlacesPresenter presenter);

    void inject(PlacesFragment placesFragment);
}