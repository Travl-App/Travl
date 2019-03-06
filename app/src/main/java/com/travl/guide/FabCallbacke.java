package com.travl.guide;

//Created by Squirty on 06.03.2019.
class FabCallbacke {

    public interface MapCallback {
        void initFab();
    }

    private MapCallback callback;

    public void registerCallBack(MapCallback callback) {
        this.callback = callback;
    }

    void doSomething() {
        callback.initFab();
    }
}