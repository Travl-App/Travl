package com.travl.guide.mvp.model.database;

import com.travl.guide.mvp.model.api.city.info.InfoCity;
import com.travl.guide.mvp.model.database.dao.InfoDao;
import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class RoomHelper {

	private InfoDao infoDao;

	@Inject //зависмоти можно задать через сеттеры
	public RoomHelper(InfoDao infoDao) {
		this.infoDao = infoDao;
	}

	public Maybe<InfoCity> getCityFromDataBase(int idCity){
		return infoDao.getInfoCity(idCity).subscribeOn(Schedulers.io());
	}

	public Single<Long> insertCityToDataBase(InfoCity city){
		return Single.create((SingleOnSubscribe<Long>)
			emitter -> {
				long changed = infoDao.insertCity(city);
				emitter.onSuccess(changed);
			}).subscribeOn(Schedulers.io());
	}
}
