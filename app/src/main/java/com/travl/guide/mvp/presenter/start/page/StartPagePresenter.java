package com.travl.guide.mvp.presenter.start.page;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.location.LocationPresenter;
import com.travl.guide.mvp.model.location.LocationReceiver;
import com.travl.guide.mvp.model.location.LocationRequester;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;
import com.travl.guide.ui.fragment.start.page.CitySpinnerListCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> implements LocationPresenter {

    @Inject
    Router router;
    @Inject
    CityRepo cityRepo;
    private Scheduler scheduler;
    @Inject
    LocationRequester locationRequester;
    private List<Disposable> disposables;

    private List<String> cityStringNames;
    private CitiesList cityObjectList;
    private int selectedCityId;
    private CitySpinnerListCreator listCreator;
    private City currentCity;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);
        disposables = new ArrayList<>();
    }

    @SuppressLint("CheckResult")
    public void loadCitiesList() {
        disposables.add(cityRepo.getCitiesList().observeOn(scheduler).subscribe(citiesList ->
                {
                    //setCityObjectList(citiesList);
                    // transformCityObjectsToCityStrings();
                    getViewState().addNamesToCitySpinner(cityStringNames);
                },
                Timber::e));
    }

    @Override
    public void setUserCoordinates(double[] coordinates) {
        if (coordinates != null) {
            locationRequester.setCoordinates(coordinates);
        }
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByCoordinates() {
        disposables.add(cityRepo.getCityContent(new CoordinatesRequest(locationRequester.getLastKnownCoordinates())).observeOn(scheduler).subscribe(cityContent -> {
            setCityContentByCoordinates(cityContent);
        }, Timber::e));
    }

    private void setCityContentByCoordinates(CityContent cityContent) {
    }

    @Override
    public void observeUserCoordinates() {
        disposables.add(locationRequester.getCoordinatesRequestPublishSubject()
                .subscribe(coordinatesRequest -> {
                    loadCityContentByCoordinates();
                }, Timber::e));
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByLinkId(int id) {
        disposables.add(cityRepo.loadCity(id).observeOn(scheduler).subscribe(cityContent ->
                setCityContentByLinkId(cityContent), Timber::e));

    }

    private void setCityContentByLinkId(CityContent cityContent) {
    }

    public void setCurrentCity(CityContent cityContent) {

    }

    public void onSpinnerItemClick(String selectedCity, String... filterStrings) {
        if (cityStringNames != null && cityObjectList != null) {
            if (cityStringNames.contains(selectedCity)) {
                for (CitiesList.CityLink link : cityObjectList.getCities()) {
                    String formattedLink = listCreator.formatCityLink(link);
                    if (selectedCity.equals(formattedLink) || (filterStrings[0] + " " + selectedCity).equals(formattedLink)) {
                        selectedCityId = link.getId();
                        loadCityContentByLinkId(link.getId());
                        return;
                    }
                }
            } else {
                String userCityName = getCityName();
                if (userCityName != null) {
                    if (selectedCity.contains(userCityName)) {
                        loadCityContentByCoordinates();
                    }
                }
            }
        }
    }

    public void addToCityList(City city, boolean isUserCity) {
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

    public void setCityArticles() {
        getViewState().setCityArticles(currentCity);
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

    public String getCityName() {
        return cityRepo.getCityName();
    }

    public void setCityName(String cityName) {
        cityRepo.saveCityName(cityName);
    }

    public void OnCityInfoButtonClick() {
	    router.navigateTo(new Screens.InfoCityScreen(selectedCityId));
    }

    public double[] getCoordinates() {
        return null;
    }

    public void onCreateView() {
        getViewState().initCityArticlesFragment();
        getViewState().initTravlZineFragment();
    }

    public void onViewStateRestored() {
        initLocationListener();
        requestCoordinates(getViewState());
        getViewState().initCitySpinner();
        loadCitiesList();
        getViewState().initMoveToNavigator();
    }
}
