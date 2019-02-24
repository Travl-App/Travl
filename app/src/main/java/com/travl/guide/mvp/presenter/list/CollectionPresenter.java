package com.travl.guide.mvp.presenter.list;

import com.travl.guide.mvp.view.list.CollectionsItemView;

import io.reactivex.subjects.PublishSubject;

//Created by Pereved on 23.02.2019.
public interface CollectionPresenter {
    PublishSubject<CollectionsItemView> getClickSubject();

    void bindView(CollectionsItemView itemView);

    int getListCount();
}