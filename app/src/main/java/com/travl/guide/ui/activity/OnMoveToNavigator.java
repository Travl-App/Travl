package com.travl.guide.ui.activity;

import com.travl.guide.navigator.CurrentScreen;

public interface OnMoveToNavigator {
    void onMoveTo(CurrentScreen.Screen currentScreen);
}
