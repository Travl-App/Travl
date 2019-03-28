package com.travl.guide.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.PlaceView;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.commands.BackTo;

@InjectViewState
public class PlacePresenter extends MvpPresenter<PlaceView> {

    @Inject
    Router router;
    @Inject
    PlacesRepo placesRepo;
    private Scheduler scheduler;
    private String argument1;
    private int argument2;

    public PlacePresenter(Scheduler scheduler, String arg1, int arg2) {
        this.scheduler = scheduler;
        this.argument1 = arg1;
        this.argument2 = arg2;
        App.getInstance().getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadPlaceCardInfo();
    }

    @SuppressLint("CheckResult")
    private void loadPlaceCardInfo() {
        placesRepo.loadPlace(argument2).observeOn(scheduler).subscribe(placeContainer -> {
            Place place = placeContainer.getPlace();
            getViewState().setPlaceAuthorNameTextView(place.getAuthor().getUserName());
            getViewState().setTextView(place.getDescription());
//                getViewState().setImageView(place.getImageUrls().get(0));
        });
    }
}
