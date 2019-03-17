package com.travl.guide.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.PlaceCardView;

import io.reactivex.Scheduler;
import timber.log.Timber;

@InjectViewState
public class PlaceCardPresenter extends MvpPresenter<PlaceCardView> {

    private Scheduler scheduler;

    private PlacesRepo placesRepo;

    public PlaceCardPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        //placesRepo = new PlacesRepo(...);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadPlaceCardInfo();
    }

    @SuppressLint("CheckResult")
    private void loadPlaceCardInfo() {
        placesRepo.loadPlaceCard("somethingParametr").observeOn(scheduler).subscribe(placeCardEntity -> {
                    getViewState().setAuthorNameTextView(placeCardEntity.getAuthorName());
                    //set other place card views
                }, Timber::e);
    }
}
