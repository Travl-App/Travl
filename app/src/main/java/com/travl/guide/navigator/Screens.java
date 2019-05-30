package com.travl.guide.navigator;

import android.support.v4.app.Fragment;

import com.travl.guide.ui.fragment.articles.ArticleFragment;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;
import com.travl.guide.ui.fragment.favorite.FavoriteFragment;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.place.PlaceFragment;
import com.travl.guide.ui.fragment.start.page.StartPageFragment;

import ru.terrakok.cicerone.Screen;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens extends Screen {

    public static class PlacesScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new TravlZineArticlesFragment();
        }
    }

    public static class MapScreen extends SupportAppScreen {

        private double[] coordinates;

        public MapScreen(double[] coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public Fragment getFragment() {
            return MapsFragment.getInstance(coordinates);
        }
    }

    public static class StartPageScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new StartPageFragment();
        }
    }

    public static class PlaceScreen extends SupportAppScreen {

        private int placeId;

        public PlaceScreen(int placeId) {
            this.placeId = placeId;
        }

        @Override
        public Fragment getFragment() {
            return PlaceFragment.getInstance(placeId);
        }
    }

    public static class FavoriteScreens extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new FavoriteFragment();
        }
    }

    public static class ArticleScreen extends SupportAppScreen {

        private int articleId;

        public ArticleScreen(int articleId) {
            this.articleId = articleId;
        }

        @Override
        public Fragment getFragment() {
            return ArticleFragment.getInstance(articleId);
        }
    }
}
