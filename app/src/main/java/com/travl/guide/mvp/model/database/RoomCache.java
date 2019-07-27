package com.travl.guide.mvp.model.database;

import com.travl.guide.mvp.model.database.models.InfoCity;
import com.travl.guide.mvp.model.database.dao.InfoDao;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RoomCache {

	private InfoDao infoDao;

	public RoomCache(InfoDao infoDao) {
		this.infoDao = infoDao;
	}


	public Maybe<InfoCity> getInfoCityFromBase(int idCity){
		return infoDao.getInfoCity(idCity).subscribeOn(Schedulers.io());
	}

	public Completable insertInfoCityToBase(InfoCity city){
		return Completable.fromAction(() -> infoDao.insertCity(city)).subscribeOn(Schedulers.io());
	}
}
