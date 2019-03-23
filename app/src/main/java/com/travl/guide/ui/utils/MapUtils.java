package com.travl.guide.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

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

    public static Bitmap SymbolGenerator(@NonNull View view) {
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);

        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();

        view.layout(0, 0, measuredWidth, measuredHeight);
        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}