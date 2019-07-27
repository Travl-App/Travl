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
import com.travl.guide.mvp.view.maps.MapsView;
import com.travl.guide.navigator.Screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
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
    private List<Disposable> disposables;

    public MapsPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        if (model == null) this.model = new MapsModel();
        disposables = new ArrayList<>();
    }

    public void onLocationComponentActivated() {
        getViewState().showUserLocation();
    }

    public void onPlaceMarkerClick(int id) {
        router.navigateTo(new Screens.PlaceScreen(id));
    }

    @SuppressLint("CheckResult")
    public void onMapBoxSetup(double[] coordinates) {
        if (coordinates == null) return;
        double latitude = coordinates[0];
        double longitude = coordinates[1];
        disposables.add(placesRepo.loadPlacesForMap(
                new CoordinatesRequest(latitude, longitude), 2000, 1)
                .observeOn(scheduler)
                .subscribe(placesMap -> {
                    PlaceContainer placeContainer = placesMap.getPlaces();
                    recursivePlacesLoading(placeContainer);
                }, Timber::e));
    }

    @SuppressLint("CheckResult")
    private void loadNextPlaces(String nextUrl) {
        disposables.add(placesRepo.loadNextPlaces(nextUrl)
                .observeOn(scheduler)
                .subscribe(placesMap -> {
                    PlaceContainer placeContainer = placesMap.getPlaces();
                    recursivePlacesLoading(placeContainer);
                }, Timber::e));
    }

    private void recursivePlacesLoading(PlaceContainer placeContainer) {
        if (placeContainer != null) {
            String nextUrl;
            int totalNumberOfItems = placeContainer.getCount();
            List<PlaceLink> links = placeContainer.getPlaceLinkList();
            if (links != null) {
                int lastItemId = links.get(links.size() - 1).getId();
                if ((nextUrl = placeContainer.getNext()) != null && lastItemId < totalNumberOfItems) {
                    loadNextPlaces(nextUrl);
                }
                addPlacesToMap(placeContainer);
            }
        }
    }

    private void addPlacesToMap(PlaceContainer placeContainer) {
        List<PlaceLink> placeLinks = placeContainer.getPlaceLinkList();
        getViewState().onPlacesLoaded(parsePlaceLinksListToFeatures(placeLinks));
    }


    private List<Feature> parsePlaceLinksListToFeatures(List<PlaceLink> places) {
        Timber.d("Parsing coordinates");
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            double[] coordinates = places.get(i).getCoordinates();
            double latitude = coordinates[0];
            double longitude = coordinates[1];
            Feature feature = Feature.fromGeometry(Point.fromLngLat(longitude, latitude));
            feature.addNumberProperty("id", places.get(i).getId());
            features.add(feature);
        }

        return features;
    }

    public void onCreate() {
        getViewState().setupMapBox();
        getViewState().setupFab();
    }

    public void onDispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    public void onLocationPermissionsGranted() {
        getViewState().showUserLocation();
    }
}