package com.travl.guide.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.view.PlaceView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import timber.log.Timber;

@InjectViewState
public class PlacePresenter extends MvpPresenter<PlaceView> {

    private Scheduler scheduler;

    @Inject
    PlacesRepo placesRepo;

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
        placesRepo.loadPlaceCard("somethingParametr").observeOn(scheduler).subscribe(placeCardEntity -> {
                    getViewState().setPlaceAuthorNameTextView(placeCardEntity.getAuthorName());
                    //set other place card views
                },
                throwable -> {
                    Timber.e(throwable);
                });
    }
}
