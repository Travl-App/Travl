package com.travl.guide.mvp.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.travl.guide.mvp.model.api.city.info.InfoCity;
import com.travl.guide.mvp.model.database.dao.InfoDao;

@Database(entities = {InfoCity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
	public abstract InfoDao infoDao();
}

