package com.travl.guide.mvp.model.location;

import android.support.annotation.NonNull;

import com.travl.guide.mvp.model.network.CoordinatesRequest;

import io.reactivex.Observable;

public interface LocationRequester {
    void initLocationListener(LocationPresenter presenter);

    void onPermissionResult(LocationPresenter presenter, boolean granted);

    void onRequestPermissionsResult(LocationPresenter presenter, int requestCode,
                                    @NonNull String[] permissions, @NonNull int[] grantResults);

    void requestCoordinates(LocationPresenter locationPresenter);

    double[] getLastKnownCoordinates();

    void setCoordinates(double[] coordinates);

    Observable<CoordinatesRequest> getCoordinatesRequestPublishSubject();

}
