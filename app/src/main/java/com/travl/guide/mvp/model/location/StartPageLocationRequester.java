package com.travl.guide.mvp.model.location;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.ui.App;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.travl.guide.util.UtilVariables.FINE_LOCATION_PERMISSION;
import static com.travl.guide.util.UtilVariables.LOCATION_PERMISSIONS_REQUEST_CODE;

public class StartPageLocationRequester implements LocationRequester {
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
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

    public void initLocationListener(LocationPresenter presenter) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    setCoordinates(new double[]{location.getLatitude(), location.getLongitude()});
                }
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
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.getInstance());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
    }
}
