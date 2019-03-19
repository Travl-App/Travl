package com.travl.guide.mvp.model.network;

import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.api.places.PlacesMap;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetService {
    @GET("api/users/{username}/places/")
    Single<PlacesMap> getPlaces(@Path("username") String user, @Query(value = "position", encoded = true) CoordinatesRequest position, @Query("radius") double radius);

    @GET("api/users/{username}/places/{id}")
    Single<Place> getPlace(@Path("username") String user, @Path("id") int id);

    //Single<PlaceEntity> getPlace(...);
}
