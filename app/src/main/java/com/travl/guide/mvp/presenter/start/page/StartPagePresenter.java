package com.travl.guide.mvp.presenter.start.page;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.location.LocationPresenter;
import com.travl.guide.mvp.model.location.LocationReceiver;
import com.travl.guide.mvp.model.location.LocationRequester;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import timber.log.Timber;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> implements LocationPresenter {

    @Inject
    CityRepo cityRepo;
    private Scheduler scheduler;
    @Inject
    LocationRequester locationRequester;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);

    }

    @SuppressLint("CheckResult")
    public void loadCitiesList() {
        cityRepo.getCitiesList().observeOn(scheduler).subscribe(citiesList -> getViewState().setCityObjectList(citiesList), Timber::e);
    }

    @Override
    public void setUserCoordinates(double[] coordinates) {
        if (coordinates != null) {
            User.getInstance().setCoordinates(coordinates);
        }
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByCoordinates(double[] coordinates) {
        Timber.e("loadCityContentByCoordinates");
        if (coordinates == null) return;
        CoordinatesRequest position = new CoordinatesRequest(coordinates);
        cityRepo.getCityContent(position).observeOn(scheduler).subscribe(cityContent -> {
            getViewState().setCityContentByCoordinates(cityContent);
        }, Timber::e);
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByLinkId(int id) {
        cityRepo.loadCity(id).observeOn(scheduler).subscribe(cityContent -> getViewState().setCityContentByLinkId(cityContent), Timber::e);

    }

    public void setCity(CityContent cityContent) {
        getViewState().setCity(cityContent);
    }

    public void requestLocationPermissions() {
        getViewState().requestLocationPermissions();
    }

    public void onSpinnerItemClick(String selectedCity) {
        getViewState().onSpinnerItemClick(selectedCity);
    }

    public void addToCityList(City city, boolean isUserCity) {
        getViewState().addToCityList(city, isUserCity);
    }

    public void setSpinnerPositionSelected(int position) {
        getViewState().setSpinnerPositionSelected(position);
    }

    public void placeSelectedCityOnTop(String placeName) {
        getViewState().placeSelectedCityOnTop(placeName);
    }

    public void removeFromCitySpinnerAdapter(String placeName) {
        getViewState().removePlaceIfIsAdded(placeName);
    }

    public void setCityArcticles() {
        getViewState().setCityArticles();
    }

    public void initLocationListener() {
        locationRequester.initLocationListener(this);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        locationRequester.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void onLocationPermissionResultGranted() {
        getViewState().onLocationPermissionRequestGranted();
    }

    public void requestCoordinates(LocationReceiver locationReceiver) {
        locationRequester.requestCoordinates(locationReceiver);
    }

    public void onLocationPermissionResult(boolean granted) {
        locationRequester.onPermissionResult(this, granted);
    }

    public void requestLocation() {
        locationRequester.requestLocation();
    }
}
