package com.travl.guide.mvp.model.location;

public interface LocationPresenter {
    void observeUserCoordinates();
    void onLocationPermissionResultGranted();
}
