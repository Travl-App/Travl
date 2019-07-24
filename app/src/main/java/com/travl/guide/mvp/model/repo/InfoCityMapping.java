package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.database.models.InfoCity;

class InfoCityMapping {

	InfoCity greatInfoCity(City city){
		InfoCity info = new InfoCity();
		info.setIdCity(city.getIdCity());
		info.setTitle(city.getTitle());
		info.setDescription(city.getDescription());
		info.setImage(city.getImage());
		return info;
	}
}
