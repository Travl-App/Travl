package com.travl.guide.mvp.presenter.list;

import com.travl.guide.mvp.view.list.PlacesItemView;

import io.reactivex.subjects.PublishSubject;

//Created by Pereved on 23.02.2019.
public interface PlacePresenter {
    PublishSubject<PlacesItemView> getClickSubject();

    void bindView(PlacesItemView itemView);

    int getListCount();
}