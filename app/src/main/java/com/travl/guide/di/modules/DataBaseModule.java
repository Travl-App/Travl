package com.travl.guide.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.travl.guide.mvp.model.database.AppDatabase;
import com.travl.guide.mvp.model.database.RoomCache;
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

	@Provides
	RoomCache provideRoomCache(InfoDao infoDao){
		return new RoomCache(infoDao);
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
