package com.travl.guide.navigator;


import android.support.v4.app.Fragment;

import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.places.PlacesFragment;

import ru.terrakok.cicerone.android.support.SupportAppScreen;

//Created by Pereved on 23.02.2019.
public class Screens {
    public static class PlacesScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return PlacesFragment.getInstance();
        }
    }

    public static class MapScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return MapsFragment.getInstance();
        }
    }
}
