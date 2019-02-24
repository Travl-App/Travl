package com.travl.guide.navigator;


import android.support.v4.app.Fragment;

import com.travl.guide.ui.fragment.collections.CollectionsFragment;
import com.travl.guide.ui.fragment.map.MapsFragment;

import ru.terrakok.cicerone.android.support.SupportAppScreen;

//Created by Pereved on 23.02.2019.
public class Screens {
    public static class CollectionScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return CollectionsFragment.getInstance();
        }
    }

    public static class MapScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return MapsFragment.getInstance();
        }
    }
}
