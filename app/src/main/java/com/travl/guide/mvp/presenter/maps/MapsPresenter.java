package com.travl.guide.mvp.presenter.maps;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.travl.guide.mvp.model.MapsModel;
import com.travl.guide.mvp.model.api.places.ManyPlacesContainer;
import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.api.places.PlaceLink;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.maps.MapsView;
import com.travl.guide.navigator.Screens;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
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

    public void toCardScreen(int placeId) {
        router.navigateTo(new Screens.PlaceScreen(placeId));
    }

    public void makeRequest() {
        SingleObserver<ManyPlacesContainer> observer = new SingleObserver<ManyPlacesContainer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ManyPlacesContainer placesMap) {
                getViewState().onPlacesLoaded(parsePlaceLinksListToFeatures(placesMap.getPlaces()));
                getViewState().onRequestCompleted(creatingPlacesList(placesMap.getPlaces()));
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
        placesRepo.loadPlacesForMap(
                new CoordinatesRequest(60, 31), 2000, 1)
                .observeOn(scheduler)
                .subscribe(observer);
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
}