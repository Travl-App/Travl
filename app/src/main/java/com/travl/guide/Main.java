package com.travl.guide;

//Created by Squirty on 06.03.2019.
class Main {
    public static void main(String[] args) {
        FabCallbacke fabCallback = new FabCallbacke();
        MapsFrag mapsFrag = new MapsFrag();

        fabCallback.registerCallBack(mapsFrag);
        fabCallback.doSomething();

    }
}
