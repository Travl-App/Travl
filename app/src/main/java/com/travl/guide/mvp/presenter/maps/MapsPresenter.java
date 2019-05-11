package com.travl.guide.mvp.presenter.maps;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.travl.guide.mvp.model.MapsModel;
import com.travl.guide.mvp.model.api.places.map.Place;
import com.travl.guide.mvp.model.api.places.map.PlaceContainer;
import com.travl.guide.mvp.model.api.places.map.PlaceLink;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.model.user.User;
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
        if (model == null) this.model = new MapsModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void setupMapView() {
        getViewState().setupMapBox();
    }

    public void showUserLocation() {
        getViewState().showUserLocation();
    }

    public void toPlaceScreen(List<Place> listPlaces, double[] coordinates) {
        Timber.d("Получены координаты: " + coordinates[0] + " " + coordinates[1]);
        router.navigateTo(new Screens.PlaceScreen(getId(listPlaces, coordinates)));
    }

    private int getId(List<Place> listPlaces, double[] coordinates) {
        for (int i = 0; i < listPlaces.size(); i++) {
            double[] local = listPlaces.get(i).getCoordinates();
            if (coarseEqualsCheck(coordinates[0], local[0]) && coarseEqualsCheck(coordinates[1], local[1])) {
                Timber.d("Id маркера: %s", String.valueOf(listPlaces.get(i).getId()));
                return listPlaces.get(i).getId();
            }
        }
        Timber.d("Всё плохо, мы ничего не нашли и выводим рандомную статью");
        return 1;
    }

    private boolean coarseEqualsCheck(double first, double second) {
        DecimalFormat roundTo = new DecimalFormat("#.##");
        int gap = 3;
        double precision = 0.01;
        for (int i = -gap; i < gap; i++) {
            String firstNumber = roundTo.format(first + i * precision);
            String secondNumber = roundTo.format(second);
            Timber.e("First coordinate =" + firstNumber + "Second coordinate =" + secondNumber);
            if (firstNumber.equals(secondNumber)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("CheckResult")
    public void makeRequestForPlaces() {
        Timber.e("Make request");
        getViewState().showLoadInfo();
        User user = User.getInstance();
        double[] coordinates = user.getCoordinates();
        if (coordinates == null) return;
        double latitude = coordinates[0];
        double longitude = coordinates[1];
        placesRepo.loadPlacesForMap(
                new CoordinatesRequest(latitude, longitude), 2000, 1)
                .observeOn(scheduler)
                .subscribe(placesMap -> {
                    PlaceContainer placeContainer = placesMap.getPlaces();
                    if (placeContainer != null) {
                        String nextUrl;
                        if ((nextUrl = placeContainer.getNext()) != null) {
                            Timber.e("Next url = " + nextUrl);
                            loadNextPlaces(nextUrl);
                        }
                        addPlacesToMap(placeContainer);
                    }
                }, Timber::e);
    }

    @SuppressLint("CheckResult")
    private void loadNextPlaces(String nextUrl) {
        placesRepo.loadNextPlaces(nextUrl)
                .observeOn(scheduler)
                .subscribe(placesMap -> {
                    PlaceContainer placeContainer = placesMap.getPlaces();
                    if (placeContainer != null) {
                        String next;
                        if ((next = placeContainer.getNext()) != null) {
                            loadNextPlaces(next);
                        }
                        addPlacesToMap(placeContainer);
                    }
                }, Timber::e);
    }

    private void addPlacesToMap(PlaceContainer placeContainer) {
        List<PlaceLink> placeLinks = placeContainer.getPlaceLinkList();
        getViewState().onPlacesLoaded(parsePlaceLinksListToFeatures(placeLinks));
        getViewState().onRequestCompleted(creatingPlacesList(placeLinks));
        getViewState().hideLoadInfo();
    }


    private List<Feature> parsePlaceLinksListToFeatures(List<PlaceLink> places) {
        Timber.d("Parsing coordinates");
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            double[] coordinates = places.get(i).getCoordinates();
            double latitude = coordinates[0];
            double longitude = coordinates[1];
            features.add(Feature.fromGeometry(Point.fromLngLat(longitude, latitude)));
        }

        return features;
    }

    private List<Place> creatingPlacesList(List<PlaceLink> places) {
        Timber.d("Creating places list");
        List<Place> placesMap = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
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