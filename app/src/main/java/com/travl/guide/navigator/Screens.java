package com.travl.guide.navigator;


import android.support.v4.app.Fragment;

import com.travl.guide.ui.fragment.Collections.CollectionsFragment;
import com.travl.guide.ui.fragment.map.MapFragment;

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
            return MapFragment.getInstance();
        }
    }
}
