package com.travl.guide.mvp.view;

import android.app.Activity;

//Created by Pereved on 06.03.2019.
public class FabCallback {

    public interface MapCallback {
        void fabClick(Activity activity);
    }

    private MapCallback callback;

    public void registerCallBack(MapCallback callback) {
        this.callback = callback;
    }

    public void clickToFab(Activity activity) {
        callback.fabClick(activity);
    }
}