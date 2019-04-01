package com.travl.guide.mvp.presenter.place;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.place.PlaceView;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PlacePresenter extends MvpPresenter<PlaceView> {

    @Inject
    Router router;
    @Inject
    PlacesRepo placesRepo;

    private Scheduler scheduler;
    private int placeId;

    public PlacePresenter(Scheduler scheduler, int placeId) {
        this.scheduler = scheduler;
        this.placeId = placeId;
        App.getInstance().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadPlaceCardInfo();
    }

    @SuppressLint("CheckResult")
    private void loadPlaceCardInfo() {
        placesRepo.loadPlace(placeId).observeOn(scheduler).subscribe(placeContainer -> {
            Place place = placeContainer.getPlace();
            getViewState().setImageSlider(place.getImageUrls());
            getViewState().setPlaceAuthorNameTextView(place.getAuthor().getUserName());
            getViewState().setTextView(place.getDescription());
            getViewState().setTitleTextView(place.getTitle());
        });
    }

    public void backPressure() {
        router.replaceScreen(new Screens.MapScreen());
    }
}
