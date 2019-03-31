package com.travl.guide.di.interfaces;

import com.travl.guide.di.modules.AppModule;
import com.travl.guide.di.modules.ArticlesRepoModule;
import com.travl.guide.di.modules.CiceroneModule;
import com.travl.guide.di.modules.CityRepoModule;
import com.travl.guide.di.modules.ImageLoaderModule;
import com.travl.guide.di.modules.NetModule;
import com.travl.guide.di.modules.PlacesRepoModule;
import com.travl.guide.mvp.presenter.articles.ArticlePresenter;
import com.travl.guide.mvp.presenter.articles.CityArticlesPresenter;
import com.travl.guide.mvp.presenter.articles.TravlZineArticlesPresenter;
import com.travl.guide.mvp.presenter.favourite.FavoritePresenter;
import com.travl.guide.mvp.presenter.main.MainPresenter;
import com.travl.guide.mvp.presenter.maps.MapsPresenter;
import com.travl.guide.mvp.presenter.place.PlacePresenter;
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.ui.activity.MainActivity;
import com.travl.guide.ui.fragment.articles.ArticleFragment;
import com.travl.guide.ui.fragment.articles.city.CityArticlesFragment;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;
import com.travl.guide.ui.fragment.drawer.BottomNavigationDrawerBehavior;
import com.travl.guide.ui.fragment.favorite.FavoriteFragment;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.place.PlaceFragment;

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

    void inject(TravlZineArticlesPresenter presenter);

    void inject(CityArticlesPresenter presenter);

    void inject(TravlZineArticlesFragment travlZineArticlesFragment);

    void inject(CityArticlesFragment travlZineArticlesFragment);

    void inject(BottomNavigationDrawerBehavior fragment);

    void inject(PlaceFragment fragment);

    void inject(PlacePresenter presenter);

    void inject(FavoriteFragment fragment);

    void inject(FavoritePresenter presenter);

    void inject(StartPagePresenter startPagePresenter);

    void inject(ArticlePresenter presenter);

    void inject(ArticleFragment fragment);
}