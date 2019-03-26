package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.repo.CityRepo;
import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.view.StartPageView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@InjectViewState
public class StartPagePresenter extends MvpPresenter<StartPageView> {

    @Inject
    CityRepo cityRepo;
    private Scheduler scheduler;

    public StartPagePresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        App.getInstance().getAppComponent().inject(this);

    }

    public void loadCityContent() {
        SingleObserver<CityContent> observer = new SingleObserver<CityContent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(CityContent cityContent) {
                int status = cityContent.getStatus();
                City city = null;
                if (status == 200) {
                    city = cityContent.getCity();
                } else if (status == 404) {
                    city = cityContent.getContext();
                }

                if (city != null) {
                    getViewState().setCityName(city.getPlaceName());
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        };
        CoordinatesRequest position = new CoordinatesRequest(User.getInstance().getCoordinates());
        cityRepo.getCityContent(User.getInstance().getDefaultUserName(), position).observeOn(scheduler).subscribe(observer);
    }


    public void initPlacesFragment() {
        getViewState().initArticlesFragment();
    }
}
