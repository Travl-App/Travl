package com.travl.guide.ui.fragment.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.leinardi.android.speeddial.SpeedDialView;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.MapsPresenter;
import com.travl.guide.mvp.view.MapsView;
import com.travl.guide.ui.App;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MapsFragment extends MvpAppCompatFragment implements MapsView {

    @SuppressLint("StaticFieldLeak")
    public static MapsFragment instance;

    public static MapsFragment getInstance() {
        if(instance == null)
            instance = new MapsFragment();
        return instance;
    }

    @InjectPresenter
    MapsPresenter presenter;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.fab_search_places)
    SpeedDialView fab;

    private MapboxMap mapBoxMap;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this, view);
        App.getInstance().getAppComponent().inject(this);
        mapView.onCreate(savedInstanceState);
        initEvents();
        return view;
    }

    private void initEvents() {
        presenter.setupMapView();
        presenter.setupFabView();
    }

    public void findPlace() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(getActivity());
        startActivityForResult(intent, 1);
    }

    @Override
    public void setupMultiFab() {
        fab.inflate(R.menu.map_fab_menu);
        fab.setOnActionSelectedListener(actionItem -> {
            switch(actionItem.getId()) {
                case R.id.fab_menu_search:
                    findPlace();
                case R.id.fab_menu_location:
                    enableLocationComponent(mapBoxMap);
                default:
                    return false;
            }
        });
    }

    @Override
    public void setupMapBox() {
        mapView.getMapAsync(mapBoxMap -> mapBoxMap.setStyle(Style.DARK, style -> {
            this.mapBoxMap = mapBoxMap;
            enableLocationComponent(mapBoxMap);
        }));
    }

    @Override
    public void requestPermissions() {
        if(! ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
            enableLocationComponent(mapBoxMap);
        }
    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent(MapboxMap mapboxMap) {
        if(PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(App.getInstance(), Objects.requireNonNull(mapboxMap.getStyle()));
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            presenter.requestPermissions();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            if(mapBoxMap != null) {
                Style style = mapBoxMap.getStyle();
                if(style != null) {
                    String geoJsonSourceLayerId = "geoJsonSourceLayerId";
                    GeoJsonSource source = style.getSourceAs(geoJsonSourceLayerId);
                    if(source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).latitude(),
                                            ((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

    @ProvidePresenter
    public MapsPresenter providePresenter() {
        MapsPresenter presenter = new MapsPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}