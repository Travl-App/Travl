package com.travl.guide.mvp.presenter.start.page;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.ui.App;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Scheduler;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> {

    @Inject
    CityRepo cityRepo;
    private Scheduler scheduler;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);

    }

    @SuppressLint("CheckResult")
    public void loadCitiesList() {
        cityRepo.getCitiesList().observeOn(scheduler).subscribe(citiesList -> getViewState().setCitiesList(citiesList));
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByCoordinates(double[] coordinates) {
        CoordinatesRequest position = new CoordinatesRequest(coordinates);
        cityRepo.getCityContent(position).observeOn(scheduler).subscribe(cityContent -> {
            getViewState().setCityContentByCoordinates(cityContent);
        });
    }


    public void initTravlZineArticlesFragment() {
        getViewState().initArticlesFragment();
    }

    public void initCityArticlesFragment() {
        getViewState().initCityArticlesFragment();
    }

    @SuppressLint("CheckResult")
    public void loadCityContentByLinkId(int id) {
        cityRepo.loadCity(id).observeOn(scheduler).subscribe(cityContent -> getViewState().setCityContentByLinkId(cityContent));

    }

    public void setCityName(City city) {
        getViewState().setCityName(city);
    }

    public void requestCoordinates() {
        getViewState().requestCoordinates();
    }

    public void requestLocation() {
        getViewState().requestLocation();
    }

    public void setCityNames(ArrayList<String> citiesListToCitiesNameList) {
        getViewState().setCityNames(citiesListToCitiesNameList);
    }

    public void onSpinnerItemClick(String selectedCity) {
        getViewState().onSpinnerItemClick(selectedCity);
    }
}
