package com.travl.guide.ui.utils;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Objects;

//Created by Squirty on 22.03.2019.
public class MapUtils {

    public static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    public static final String PLACE_IMAGE = "place_image";
    public static final String MARKER_LAYER = "marker-layer";
    public static final String PLACES_GEO_SOURCE = "places_geo_source";
    public static final String CALLOUT_LAYER_ID = "mapbox.poi.callout";

    public static LatLng convertToLatLng(Feature feature) {
        Point symbolPoint = (Point) feature.geometry();
        return new LatLng(Objects.requireNonNull(symbolPoint).latitude(), symbolPoint.longitude());
    }
}
