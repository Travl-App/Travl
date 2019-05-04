package com.travl.guide.mvp.model.location;

public interface LocationPresenter {
    void setUserCoordinates(double[] doubles);

    void loadCityContentByCoordinates(double[] coordinates);

    void onLocationPermissionResultGranted();
}
