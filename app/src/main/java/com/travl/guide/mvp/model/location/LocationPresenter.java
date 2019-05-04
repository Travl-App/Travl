package com.travl.guide.mvp.model.location;

public interface LocationPresenter {
    void setUserCoordinates(double[] doubles);

    void observeUserCoordinates();

    void onLocationPermissionResultGranted();
}
