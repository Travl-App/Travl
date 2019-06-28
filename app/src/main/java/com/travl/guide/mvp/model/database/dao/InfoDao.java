package com.travl.guide.mvp.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.travl.guide.mvp.model.api.city.info.InfoCity;

import io.reactivex.Maybe;

@Dao
public interface InfoDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	long insertCity(InfoCity city);

	@Query("SELECT * FROM infoCity WHERE idCity = :idCity")
	Maybe<InfoCity> getInfoCity(int idCity);
}
