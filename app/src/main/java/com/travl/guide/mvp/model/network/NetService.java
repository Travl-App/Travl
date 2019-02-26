package com.travl.guide.mvp.model.network;

import com.travl.guide.mvp.model.places.PlacesMap;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetService {
    @GET("/user/{username}/places")
    PlacesMap getPlaces(@Path("username") String user, @Query("position") String position, @Query("radius") double radius);
}
