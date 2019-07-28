package com.travl.guide.mvp.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.travl.guide.mvp.model.api.city.info.InfoCity;

import io.reactivex.Maybe;

@Dao
public interface InfoDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long insertCity(InfoCity city);

	@Query("SELECT * FROM infoCity WHERE idCity = :idCity")
	Maybe<InfoCity> getInfoCity(int idCity);
}
