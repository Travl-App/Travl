package com.travl.guide.ui.fragment.start.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.ui.App;

import timber.log.Timber;

class StartPageLocationManager {
    private static final String COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 0;
    private LocationListener mListener;
    private StartPagePresenter presenter;
    private LocationManager locationManager;

    StartPageLocationManager(StartPagePresenter presenter) {
        this.presenter = presenter;
        locationManager = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
    }

    void initLocationListener() {
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Timber.e("Location changed:" + location.getLatitude() + "," + location.getLongitude());
                presenter.setUserCoordinates(new double[]{location.getLatitude(), location.getLongitude()});
                presenter.loadCityContentByCoordinates(User.getInstance().getCoordinates());
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


    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.e("onRequestPermissionsResult");
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            onPermissionResult(granted);
        }
    }


    @SuppressLint("MissingPermission")
    void requestCoordinates(Activity activity) {
        boolean locationPermissionGranted = ContextCompat.checkSelfPermission(App.getInstance(), FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
        if (locationPermissionGranted) {
            requestLocation();
        } else if (!locationPermissionGranted) {
            Timber.e("PermissionNotGranted");
            if (activity != null) {
                ActivityCompat.requestPermissions(activity, new String[]{COARSE_LOCATION_PERMISSION, FINE_LOCATION_PERMISSION}, LOCATION_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    void onPermissionResult(boolean granted) {
        Timber.e("onPermissionResult %s", granted);
        if (granted) {
            presenter.requestCoordinates();
        }
    }


    @SuppressLint("MissingPermission")
    void requestLocation() {
        Timber.e("requestLocation");
        int millisInSecond = 1000;
        int minutes = 1;
        int secondsInMinutes = 30;
        int meters = 100;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String gpsProvider = LocationManager.GPS_PROVIDER;
        if (locationManager.isProviderEnabled(networkProvider)) {
            locationManager.requestLocationUpdates(
                    networkProvider, minutes * secondsInMinutes * millisInSecond, meters, mListener);
        } else {
            locationManager.requestLocationUpdates(
                    gpsProvider, minutes * secondsInMinutes * millisInSecond, meters, mListener);
        }
    }
}
