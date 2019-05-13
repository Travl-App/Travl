package com.travl.guide.mvp.presenter.place;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.places.articles.Place;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.place.PlaceView;
import com.travl.guide.ui.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class PlacePresenter extends MvpPresenter<PlaceView> {


    @Inject
    @Named("baseUrl")
    String baseUrl;
    @Inject
    Router router;
    @Inject
    PlacesRepo placesRepo;

    private Scheduler scheduler;
    private int placeId;
    private List<Disposable> disposables;

    public PlacePresenter(Scheduler scheduler, int placeId) {
        this.scheduler = scheduler;
        this.placeId = placeId;
        App.getInstance().getAppComponent().inject(this);
        disposables = new ArrayList<>();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadPlaceCardInfo();
    }

    @SuppressLint("CheckResult")
    private void loadPlaceCardInfo() {
        disposables.add(placesRepo.loadNewPlace(placeId).observeOn(scheduler).subscribe(root -> {
            Place place = root.getPlace();
            if (place != null) {
                List<Double> coordinates = place.getCoordinates();
                getViewState().setPlaceCoordinates(new double[]{coordinates.get(0), coordinates.get(1)});
                getViewState().setPlaceTitle(place.getTitle());
                getViewState().setPlaceImages(place.getImages());
                getViewState().setPlaceAuthorName(place.getAuthor().getUsername());
                getViewState().setPlaceDescription(place.getDescription());
            }
        }, Timber::e));
    }

    public String getPlaceUrl() {
        return baseUrl + "places/" + placeId;
    }

    public void onDispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}
