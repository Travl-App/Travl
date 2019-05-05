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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> implements LocationPresenter {

    @Inject
    CityRepo cityRepo;
    private Scheduler scheduler;
    @Inject
    LocationRequester locationRequester;
    private List<Disposable> disposables;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);
        disposables = new ArrayList<>();
    }

    @SuppressLint("CheckResult")
    public void loadCitiesList() {
        disposables.add(cityRepo.getCitiesList().observeOn(scheduler).subscribe(citiesList ->
                {
                    getViewState().setCityObjectList(citiesList);
                    getViewState().transformCityObjectsToCityStrings();
                    getViewState().addNamesToCitySpinner();
                },
                Timber::e));
    }

    @Override
    public void setUserCoordinates(double[] coordinates) {
        if (coordinates != null) {
            User.getInstance().setCoordinates(coordinates);
        }
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByCoordinates(CoordinatesRequest coordinatesRequest) {
        Timber.e("loadCityContentByCoordinates");
        disposables.add(cityRepo.getCityContent(coordinatesRequest).observeOn(scheduler).subscribe(cityContent -> {
            getViewState().setCityContentByCoordinates(cityContent);
        }, Timber::e));
    }

    @Override
    public void observeUserCoordinates() {
        disposables.add(User.getInstance().getCoordinatesRequestPublishSubject()
                .subscribe(this::loadCityContentByCoordinates, Timber::e));
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByLinkId(int id) {
        disposables.add(cityRepo.loadCity(id).observeOn(scheduler).subscribe(cityContent -> getViewState().setCityContentByLinkId(cityContent), Timber::e));

    }

    public void setCurrentCity(CityContent cityContent) {
        getViewState().setCurrentCity(cityContent);
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

    public void onDispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    public void editPreviousUserCityName(String placeName) {
        getViewState().editPreviousUserCityName(placeName);
    }
}
