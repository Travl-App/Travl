package com.travl.guide.di.interfaces;

import com.travl.guide.di.modules.AppModule;
import com.travl.guide.di.modules.ArticlesRepoModule;
import com.travl.guide.di.modules.CiceroneModule;
import com.travl.guide.di.modules.CityRepoModule;
import com.travl.guide.di.modules.ImageLoaderModule;
import com.travl.guide.di.modules.NetModule;
import com.travl.guide.di.modules.PlacesRepoModule;
import com.travl.guide.mvp.presenter.ArticlesPresenter;
import com.travl.guide.mvp.presenter.FavoritePresenter;
import com.travl.guide.mvp.presenter.MainPresenter;
import com.travl.guide.mvp.presenter.MapsPresenter;
import com.travl.guide.mvp.presenter.PlacePresenter;
import com.travl.guide.mvp.presenter.StartPagePresenter;
import com.travl.guide.ui.fragment.drawer.BottomNavigationDrawerBehavior;
import com.travl.guide.ui.activity.MainActivity;
import com.travl.guide.ui.fragment.favorite.FavoriteFragment;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.place.PlaceFragment;
import com.travl.guide.ui.fragment.places.ArticlesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        CiceroneModule.class,
        NetModule.class,
        PlacesRepoModule.class,
        ImageLoaderModule.class,
        CityRepoModule.class,
        ArticlesRepoModule.class
})
public interface AppComponent {

    void inject(MainPresenter mainPresenter);

    void inject(MainActivity mainActivity);

    void inject(MapsFragment mapFragment);

    void inject(MapsPresenter mapPresenter);

    void inject(ArticlesPresenter presenter);

    void inject(ArticlesFragment articlesFragment);

    void inject(BottomNavigationDrawerBehavior fragment);

    void inject(PlaceFragment fragment);

    void inject(PlacePresenter presenter);

    void inject(FavoriteFragment fragment);

    void inject(FavoritePresenter presenter);

    void inject(StartPagePresenter startPagePresenter);
}