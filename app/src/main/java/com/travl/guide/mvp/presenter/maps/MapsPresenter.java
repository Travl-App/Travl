package com.travl.guide.mvp.presenter.maps;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
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

    @Inject Router router;
    @Inject PlacesRepo placesRepo;

    private MapsModel model; //Todo зачем?
    private Scheduler scheduler;
    private List<Disposable> disposables;

    public MapsPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        if (model == null) this.model = new MapsModel();
        disposables = new ArrayList<>();
    }

    //ToDo зачем здесь метод?
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

	//ToDo зачем здесь метод?
    public void setupMapView() {
//        getViewState().setupMapBox();
    }

	//ToDo зачем здесь метод?
    public void showUserLocation() {
        getViewState().showUserLocation();
    }

	//ToDo зачем здесь метод?
    public void toPlaceScreen(int id) {
        router.navigateTo(new Screens.PlaceScreen(id));
    }

    public void makeRequestForPlaces(double[] coordinates) {
        getViewState().showLoadInfo();
        Timber.i("Maps received coordinates = " + Arrays.toString(coordinates) + " from locationRequester");
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

    private void loadNextPlaces(String nextUrl) {
        disposables.add(placesRepo.loadNextPlaces(nextUrl)
                .observeOn(scheduler)
                .subscribe(placesMap -> {
                    PlaceContainer placeContainer = placesMap.getPlaces();
                    recursivePlacesLoading(placeContainer);
                }, Timber::e));
    }

    private void recursivePlacesLoading(PlaceContainer placeContainer) {
        if (placeContainer == null) return;
        String nextUrl;
        int totalNumberOfItems = placeContainer.getCount();
        List<PlaceLink> links = placeContainer.getPlaceLinkList();
        if (links != null) {
	        int lastItemId = links.get(links.size() - 1).getId();
	        boolean next = (nextUrl = placeContainer.getNext()) != null && lastItemId < totalNumberOfItems;
	        if (next) {
		        loadNextPlaces(nextUrl);
	        }
	        addPlacesToMap(placeContainer, !next);
        }
    }

    private void addPlacesToMap(PlaceContainer placeContainer, boolean isLast) {
        List<PlaceLink> placeLinks = placeContainer.getPlaceLinkList();
        getViewState().onPlacesLoaded(placeLinks, isLast);
        getViewState().onRequestCompleted(creatingPlacesList(placeLinks)); //Todo зачем?
        getViewState().hideLoadInfo();
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
//        getViewState().setupFab();
    }

    public void onDispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}