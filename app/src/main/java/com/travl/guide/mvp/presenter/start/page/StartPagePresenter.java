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
import com.travl.guide.ui.activity.CoordinatesProvider;
import com.travl.guide.ui.fragment.start.page.CitySpinnerListCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> implements LocationPresenter, LocationReceiver {

    @Inject
    Router router;
    @Inject
    CityRepo cityRepo;
    private Scheduler scheduler;
    @Inject
    LocationRequester locationRequester;
    private List<Disposable> disposables;
    private static final int CODE_OK = 200;
    private static final int CODE_ERROR = 404;
    private List<String> cityStringNames;
    private CitiesList cityObjectList;
    private int selectedCityId;
    private CitySpinnerListCreator listCreator;
    private City currentCity;
    private double[] citySelectedCoordinates;
    private CityContent cityContent;
    private String selectedCity;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);
        disposables = new ArrayList<>();
    }

    @Override
    public void requestLocationPermissions() {
        getViewState().requestLocationPermissions();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    public void addNamesToCitySpinner() {
        getViewState().addNamesToCitySpinner(cityStringNames);
    }

    @Override
    public void observeUserCoordinates() {
        disposables.add(locationRequester.getCoordinatesRequestPublishSubject()
                .subscribe(coordinatesRequest -> {
                    loadCityContentByCoordinates();
                }, Timber::e));
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

    public void placeSelectedCityOnTop(String placeName) {
        cityStringNames.remove(placeName);
        cityStringNames.add(0,placeName);
        getViewState().addNamesToCitySpinner(cityStringNames);
    }

    public void removeCityFromList(String placeName) {
        cityStringNames.remove(placeName);
        getViewState().addNamesToCitySpinner(cityStringNames);
    }

    public void onAttachCityArticlesFragment() {
        getViewState().setCityArticles(currentCity);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        locationRequester.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void onLocationPermissionResultGranted() {
        requestCoordinates(this);
    }

    public void onLocationPermissionResult(boolean granted) {
        locationRequester.onPermissionResult(this, granted);
    }

    public void onDispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    public void OnCityInfoButtonClick() {
        router.navigateTo(new Screens.InfoCityScreen(selectedCityId));
    }

    public double[] getCoordinates() {
        return citySelectedCoordinates;
    }

    public void onCreateView() {
        getViewState().initCityArticlesFragment();
        getViewState().initTravlZineFragment();
    }

    public void onViewStateRestored() {
        initLocationListener();
        requestCoordinates(this);
        getViewState().initCitySpinner();
        loadCitiesList();
        getViewState().initMoveToNavigator();
    }

    @SuppressLint("CheckResult")
    private void loadCitiesList() {
        disposables.add(cityRepo.getCitiesList().observeOn(scheduler).subscribe(citiesList ->
                {
                    cityObjectList = citiesList;
                    cityStringNames = listCreator.citiesListToCitiesNameList(cityObjectList);
                    getViewState().addNamesToCitySpinner(cityStringNames);
                },
                Timber::e));
    }

    @SuppressLint("CheckResult")
    private void loadCityContentByCoordinates() {
        disposables.add(cityRepo.getCityContent(new CoordinatesRequest(locationRequester.getLastKnownCoordinates())).observeOn(scheduler).subscribe(cityContent -> {
            setCityContentByCoordinates(cityContent);
        }, Timber::e));
    }

    private void setCityContentByCoordinates(CityContent cityContent) {
        this.cityContent = cityContent;
        getViewState().hideCityArticlesFragment();
        setCurrentCity(cityContent);
        addToCityList(currentCity, true);
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(currentCity));
        this.selectedCity = cityName;
        setCityName(cityName);
        getViewState().setCityArticles(currentCity);
        getViewState().showCitiesList();
    }

    @SuppressLint("CheckResult")
    private void loadCityContentByLinkId(int id) {
        disposables.add(cityRepo.loadCity(id).observeOn(scheduler).subscribe(cityContent ->
                setCityContentByLinkId(cityContent), Timber::e));

    }

    private void setCityContentByLinkId(CityContent cityContent) {
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(cityContent.getCity()));
        //If no city is selected or loaded and if the info is related to the city selected
        if (selectedCity == null || cityName != null && (cityName.equals(selectedCity)
                || cityName.equals("" + " " + selectedCity))) {
            this.cityContent = cityContent;
            getViewState().hideCityArticlesFragment();
            setCurrentCity(cityContent);
            placeSelectedCityOnTop(cityName);
            getViewState().setCityArticles(currentCity);
        }
    }

    private void addToCityList(City city, boolean isUserCity) {
        listCreator.addToCityList(city, isUserCity, cityStringNames);
        getViewState().addNamesToCitySpinner(cityStringNames);

    }

    private void initLocationListener() {
        locationRequester.initLocationListener(this);
    }

    private void requestCoordinates(LocationReceiver locationReceiver) {
        locationRequester.requestCoordinates(locationReceiver);
    }


    private String getCityName() {
        return cityRepo.getCityName();
    }

    private void setCityName(String cityName) {
        cityRepo.saveCityName(cityName);
    }

    private void setCurrentCity(CityContent cityContent) {
        if (cityContent != null) {
            int status = cityContent.getStatus();
            if (status == CODE_OK) {
                currentCity = cityContent.getCity();
                citySelectedCoordinates = currentCity.getCoordinates();
            } else if (status == CODE_ERROR) {
                currentCity = cityContent.getContext();
                citySelectedCoordinates = new double[]{currentCity.getLatitude(), currentCity.getLongitude()};
            }
        }
    }
}
