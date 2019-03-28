package com.travl.guide.mvp.model.network;

import com.travl.guide.mvp.model.api.articles.Articles;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.api.places.Place;
import com.travl.guide.mvp.model.api.places.PlacesMap;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetService {
    @GET("api/map/")
    Single<PlacesMap> getPlacesForMap(@Query(value = "position", encoded = true) CoordinatesRequest position, @Query("radius") double radius, @Query("detailed") int detailed);

    @GET("api/places/{id}")
    Single<Place> getPlace(@Path("id") int id);

    @GET("api/query/")
    Single<CityContent> getCityContent(@Query(value = "position", encoded = true) CoordinatesRequest position);

    @GET("api/articles/")
    Single<Articles> getArticles();
}
