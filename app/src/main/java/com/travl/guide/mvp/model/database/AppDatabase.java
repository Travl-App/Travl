package com.travl.guide.mvp.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.travl.guide.mvp.model.api.city.info.InfoCity;
import com.travl.guide.mvp.model.database.dao.InfoDao;

@Database(entities = {InfoCity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
	public abstract InfoDao infoDao();
}

