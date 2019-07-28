package com.travl.guide.mvp.model.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.ui.App;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.travl.guide.util.UtilVariables.FINE_LOCATION_PERMISSION;
import static com.travl.guide.util.UtilVariables.LOCATION_PERMISSIONS_REQUEST_CODE;

public class StartPageLocationRequester implements LocationRequester {
    private LocationListener mListener;
    private LocationManager locationManager;
    private PublishSubject<CoordinatesRequest> coordinatesRequestPublishSubject = PublishSubject.create();
    private double[] coordinates;

    @Nullable
    public double[] getLastKnownCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
        coordinatesRequestPublishSubject.onNext(new CoordinatesRequest(coordinates));
    }

    @Override
    public Observable<CoordinatesRequest> getCoordinatesRequestPublishSubject() {
        return coordinatesRequestPublishSubject.subscribeOn(Schedulers.io());
    }

    public StartPageLocationRequester(App instance) {
        locationManager = (LocationManager) instance.getSystemService(Context.LOCATION_SERVICE);
    }

    public void initLocationListener(LocationPresenter presenter) {
        presenter.observeUserCoordinates();
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Timber.e("onLocationChanged");
                if (location != null) {
                    setCoordinates(new double[]{location.getLatitude(), location.getLongitude()});
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
    }


    public void onRequestPermissionsResult(LocationPresenter presenter, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            onPermissionResult(presenter, granted);
        }
    }


    @SuppressLint("MissingPermission")
    public void requestCoordinates(LocationPresenter locationPresenter) {
        boolean locationPermissionGranted = ContextCompat.checkSelfPermission(App.getInstance(), FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
        if (locationPermissionGranted) {
            requestLocation();
        } else if (!locationPermissionGranted) {
            if (locationPresenter != null) {
                locationPresenter.requestLocationPermissions();
            }
        }
    }

    public void onPermissionResult(LocationPresenter presenter, boolean granted) {
        if (granted) {
            presenter.onLocationPermissionResultGranted();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        int millisInSecond = 1000;
        int minutes = 1;
        int secondsInMinutes = 60;
        int amplifier = 10;
        int meters = 10;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String gpsProvider = LocationManager.GPS_PROVIDER;
        String passiveProvider = LocationManager.PASSIVE_PROVIDER;
        if (locationManager.isProviderEnabled(passiveProvider)) {
            Timber.e("passive");
            locationManager.requestLocationUpdates(passiveProvider,
                    minutes * secondsInMinutes * millisInSecond / amplifier, meters, mListener);
        }
        if (locationManager.isProviderEnabled(networkProvider)) {
            Timber.e("networkProvider");
            locationManager.requestLocationUpdates(networkProvider,
                    minutes * secondsInMinutes * millisInSecond / amplifier, meters, mListener);
        }

        if (locationManager.isProviderEnabled(gpsProvider)) {
            Timber.e("gpsProvider");
            locationManager.requestLocationUpdates(gpsProvider,
                    minutes * secondsInMinutes * millisInSecond / amplifier, meters, mListener);
        }

    }
}
