package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.repo.PlacesRepo;
import com.travl.guide.mvp.presenter.list.PlacePresenter;
import com.travl.guide.mvp.view.PlacesView;
import com.travl.guide.mvp.view.list.PlacesItemView;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;
import ru.terrakok.cicerone.Router;

//Created by Pereved on 18.02.2019.
@InjectViewState
public class PlacesPresenter extends MvpPresenter<PlacesView> {

    @Inject
    Router router;
    private Scheduler scheduler;
    public PlacePresenter placePresenter;
    @Inject
    PlacesRepo repo;

    public PlacesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        placePresenter = new ListPresenter();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public class ListPresenter implements PlacePresenter {

        PublishSubject<PlacesItemView> clickSubject = PublishSubject.create();

        @Override
        public PublishSubject<PlacesItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(PlacesItemView view) {
            //TODO: запрос строки из БД
//            Repository repository = user.getRepos().get(view.getPos());
//            view.setTitle(repository.getName());
//            view.setImage(repository.getUrl);
        }

        @Override
        public int getListCount() {
            //user == null || user.getDB() == null ? 0 : user.getDB().size();
            return 0;
        }
    }
}