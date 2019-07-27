package com.travl.guide.mvp.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.travl.guide.mvp.model.database.models.InfoCity;

import io.reactivex.Maybe;

@Dao
public interface InfoDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertCity(InfoCity city);

	@Query("SELECT * FROM infoCity WHERE idCity = :idCity")
	Maybe<InfoCity> getInfoCity(int idCity);
}
