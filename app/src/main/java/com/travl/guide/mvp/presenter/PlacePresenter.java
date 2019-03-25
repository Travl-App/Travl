package com.travl.guide.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.PlaceView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

@InjectViewState
public class PlacePresenter extends MvpPresenter<PlaceView> {

    @Inject
    PlacesRepo placesRepo;
    private Scheduler scheduler;

    public PlacePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadPlaceCardInfo();
    }

    @SuppressLint("CheckResult")
    private void loadPlaceCardInfo() {
        placesRepo.loadPlace("travl", 1).observeOn(scheduler).subscribe(new Consumer<Place>() {
            @Override
            public void accept(Place place) throws Exception {
                getViewState().setPlaceAuthorNameTextView(place.getAuthor());
                getViewState().setTextView(place.getDescription());
//                getViewState().setImageView(place.getImageUrls().get(0));
            }
        });
    }
}
