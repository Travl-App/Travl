package com.travl.guide;

//Created by Squirty on 06.03.2019.
class MapsFrag implements FabCallbacke.MapCallback {

    @Override
    public void initFab() {
        System.out.println("Вызов метода обратного вызова");
    }
}
