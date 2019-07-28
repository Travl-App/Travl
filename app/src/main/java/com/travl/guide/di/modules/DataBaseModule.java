package com.travl.guide.di.modules;

import android.content.Context;

import androidx.room.Room;

import com.travl.guide.mvp.model.database.AppDatabase;
import com.travl.guide.mvp.model.database.RoomHelper;
import com.travl.guide.mvp.model.database.dao.InfoDao;
import com.travl.guide.ui.App;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

	private Context context;

	public DataBaseModule() {
		this.context = App.getInstance().getApplicationContext();
	}

	@Provides //здесь наверное можно инжектировать и через сеттеры, не знаю какие запросы будут в базу
	RoomHelper provideRoomHelper(){
		return new RoomHelper(getCityDao());
	}

	@Provides
	InfoDao getCityDao(){
		return getAppDatabase().infoDao();
	}

	@Provides
	AppDatabase getAppDatabase() {
		return Room.databaseBuilder(context, AppDatabase.class, "DataBase").build();
	}
}
