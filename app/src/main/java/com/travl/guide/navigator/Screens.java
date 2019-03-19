package com.travl.guide.navigator;

import android.support.v4.app.Fragment;

import com.travl.guide.ui.fragment.PlaceFragment;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.places.ArticlesFragment;
import com.travl.guide.ui.fragment.start.page.StartPageFragment;

import ru.terrakok.cicerone.Screen;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens extends Screen {
    public static class PlacesScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new ArticlesFragment();
        }
    }

    public static class MapScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new MapsFragment();
        }
    }

    public static class StartPageScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new StartPageFragment();
        }
    }

    public static class PlaceScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new PlaceFragment();
        }
    }
}
