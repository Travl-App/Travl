package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.travl.guide.mvp.model.MapsModel;
import com.travl.guide.mvp.model.api.places.PlaceLink;
import com.travl.guide.mvp.model.api.places.PlacesMap;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.MapsView;

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

    public void loadPlacesForMap() {
        SingleObserver<PlacesMap> observer = new SingleObserver<PlacesMap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(PlacesMap placesMap) {
                getViewState().onPlacesLoaded(parsePlaceLinksListToFeautures(placesMap.getPlaces()));
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
        placesRepo.loadPlacesForMap("travl", new CoordinatesRequest(60, 31), 7, 1).observeOn(scheduler).subscribe(observer);
    }

    private List<Feature> parsePlaceLinksListToFeautures(List<PlaceLink> places) {
        Timber.e("Parsing coordinates");
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            PlaceLink place = places.get(i);
            double[] coordinates = place.getCoordinates();
            double longitude = coordinates[1];
            double latitude = coordinates[0];
            features.add(Feature.fromGeometry(Point.fromLngLat(longitude, latitude)));
        }
        return features;
    }
}