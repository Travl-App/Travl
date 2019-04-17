package com.travl.guide.mvp.presenter.maps;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.travl.guide.mvp.model.MapsModel;
import com.travl.guide.mvp.model.api.places.map.Place;
import com.travl.guide.mvp.model.api.places.map.PlaceLink;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.maps.MapsView;
import com.travl.guide.navigator.Screens;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class MapsPresenter extends MvpPresenter<MapsView> {

    @Inject
    Router router;
    @Inject
    PlacesRepo placesRepo;
    private MapsModel model;
    private Scheduler scheduler;

    public MapsPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        if(model == null) this.model = new MapsModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void setupMapView() {
        getViewState().setupMapBox();
    }

    public void showLocations() {
        getViewState().findUser();
    }

    public void toCardScreen(List<Place> listPlaces, double[] coordinates) {
        Timber.d("Получены координаты: " + String.valueOf(coordinates[0]) + " " + String.valueOf(coordinates[1]));
        router.navigateTo(new Screens.PlaceScreen(getId(listPlaces, coordinates)));
    }

    private int getId(List<Place> listPlaces, double[] coordinates) {
        for(int i = 0; i < listPlaces.size(); i++) {
            DecimalFormat roundTo = new DecimalFormat("#.###");
            double[] local = listPlaces.get(i).getCoordinates();

            if(roundTo.format(coordinates[0]).equals(roundTo.format(local[0])) && roundTo.format(coordinates[1]).equals(roundTo.format(local[1]))) {
                Timber.d("Id маркера: %s", String.valueOf(listPlaces.get(i).getId()));
                return listPlaces.get(i).getId();
            }
        }
        Timber.d("Всё плохо, мы ничего не нашли и выводим рандомную статью");
        return 1;
    }

    @SuppressLint("CheckResult")
    public void makeRequest() {
        getViewState().showLoadInfo();
        placesRepo.loadPlacesForMap(
                new CoordinatesRequest(60, 31), 2000, 1)
                .observeOn(scheduler)
                .subscribe(placesMap -> {
                    List<PlaceLink> placeLinks = placesMap.getPlaces().getPlaceLinkList();
                    getViewState().onPlacesLoaded(parsePlaceLinksListToFeatures(placeLinks));
                    getViewState().onRequestCompleted(creatingPlacesList(placeLinks));
                    getViewState().hideLoadInfo();
                }, Timber::e);
    }

    private List<Feature> parsePlaceLinksListToFeatures(List<PlaceLink> places) {
        Timber.d("Parsing coordinates");
        List<Feature> features = new ArrayList<>();
        for(int i = 0; i < places.size(); i++) {
            double[] coordinates = places.get(i).getCoordinates();
            double longitude = coordinates[1];
            double latitude = coordinates[0];
            features.add(Feature.fromGeometry(Point.fromLngLat(longitude, latitude)));
        }

        return features;
    }

    private List<Place> creatingPlacesList(List<PlaceLink> places) {
        Timber.d("Creating places list");
        List<Place> placesMap = new ArrayList<>();
        for(int i = 0; i < places.size(); i++) {
            Place place = new Place();
            place.setId(places.get(i).getId());
            place.setImageUrls(places.get(i).getImageUrls());
            place.setDescription(places.get(i).getDescription());
            place.setCoordinates(places.get(i).getCoordinates());
            placesMap.add(place);
        }

        return placesMap;
    }

    public void setupLocationFab() {
        getViewState().setupFab();
    }
}