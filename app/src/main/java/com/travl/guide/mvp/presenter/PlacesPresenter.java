package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.places.Place;
import com.travl.guide.mvp.model.places.PlacesMap;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.presenter.list.PlacePresenter;
import com.travl.guide.mvp.view.PlacesView;
import com.travl.guide.mvp.view.list.PlacesItemView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class PlacesPresenter extends MvpPresenter<PlacesView> {

    @Inject
    Router router;
    private Scheduler scheduler;
    public PlacePresenter placePresenter;
    @Inject
    PlacesRepo repo;
    @Inject
    @Named("baseUrl")
    String baseUrl;

    public PlacesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        placePresenter = new ListPresenter();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void loadPlaces() {
        Timber.d("Loading places");
        SingleObserver<PlacesMap> observer = new SingleObserver<PlacesMap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(PlacesMap placesMap) {
                placePresenter.setPlacesList(placesMap.getPlaces());
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
        repo.loadPlaces("travl", new CoordinatesRequest(59.89432427, 30.27730692), 1.0).observeOn(scheduler).subscribe(observer);
    }

    public class ListPresenter implements PlacePresenter {
        PublishSubject<PlacesItemView> clickSubject = PublishSubject.create();
        private List<Place> placeList;

        @Override
        public PublishSubject<PlacesItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(PlacesItemView view) {
            Timber.d("BindView and set Description");
            Place place = placeList.get(view.getPos());
            //TODO Decide how should we show multiple images
            int firstImage = 0;
            int redundantEscapeCharacterSkip = 1;
            List<String> imageUrls = place.getImageUrls();
            if (imageUrls != null) {
                view.setImage(baseUrl + imageUrls.get(firstImage).substring(redundantEscapeCharacterSkip));
            }
            view.setDescription(place.getDescription());
        }

        @Override
        public int getListCount() {
            Timber.d("PlaceList size = %s", (placeList == null ? null : placeList.size()));
            return placeList == null ? 0 : placeList.size();
        }

        @Override
        public void setPlacesList(List<Place> places) {
            this.placeList = places;
            getViewState().onChangedPlacesData();
        }
    }
}