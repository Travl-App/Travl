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

import com.travl.guide.ui.App;

import timber.log.Timber;

import static com.travl.guide.util.UtilVariables.COARSE_LOCATION_PERMISSION;
import static com.travl.guide.util.UtilVariables.FINE_LOCATION_PERMISSION;
import static com.travl.guide.util.UtilVariables.LOCATION_PERMISSIONS_REQUEST_CODE;

public class StartPageLocationRequester implements LocationRequester {
    private LocationListener mListener;
    private LocationManager locationManager;

    public StartPageLocationRequester(App instance) {
        locationManager = (LocationManager) instance.getSystemService(Context.LOCATION_SERVICE);
    }

    public void initLocationListener(LocationPresenter presenter) {
        Timber.e("InitLocationListener");
        presenter.observeUserCoordinates();
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Timber.e("Location changed:" + location.getLatitude() + "," + location.getLongitude());
                presenter.setUserCoordinates(new double[]{location.getLatitude(), location.getLongitude()});
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
        Timber.e("onRequestPermissionsResult");
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            onPermissionResult(presenter, granted);
        }
    }


    @SuppressLint("MissingPermission")
    public void requestCoordinates(LocationReceiver activity) {
        boolean locationPermissionGranted = ContextCompat.checkSelfPermission(App.getInstance(), FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
        if (locationPermissionGranted) {
            requestLocation();
        } else if (!locationPermissionGranted) {
            Timber.e("PermissionNotGranted");
            if (activity != null) {
                activity.requestPermissions(new String[]{COARSE_LOCATION_PERMISSION, FINE_LOCATION_PERMISSION}, LOCATION_PERMISSIONS_REQUEST_CODE);

            }
        }
    }

    public void onPermissionResult(LocationPresenter presenter, boolean granted) {
        Timber.e("onLocationPermissionResult %s", granted);
        if (granted) {
            presenter.onLocationPermissionResultGranted();
        }
    }

    @SuppressLint("MissingPermission")
    public void requestLocation() {
        Timber.e("requestLocation");
        int millisInSecond = 1000;
        int minutes = 1;
        int secondsInMinutes = 30;
        int meters = 100;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String gpsProvider = LocationManager.GPS_PROVIDER;
        String passiveProvider = LocationManager.PASSIVE_PROVIDER;
        if (locationManager.isProviderEnabled(passiveProvider)) {
            Timber.e("Passive provider");
            locationManager.requestLocationUpdates(passiveProvider,
                    minutes * secondsInMinutes * millisInSecond, meters, mListener);
        }
        if (locationManager.isProviderEnabled(networkProvider)) {
            Timber.e("Network provider");
            locationManager.requestLocationUpdates(networkProvider,
                    minutes * secondsInMinutes * millisInSecond, meters, mListener);
        } else {
            Timber.e("GPS provider");
            locationManager.requestLocationUpdates(gpsProvider,
                    minutes * secondsInMinutes * millisInSecond, meters, mListener);
        }

    }
}
