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
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
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
import com.travl.guide.mvp.view.FabCallback;
import com.travl.guide.mvp.view.MapsView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.MainActivity;

import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MapsFragment extends MvpAppCompatFragment implements MapsView {

    @InjectPresenter
    MapsPresenter presenter;
    @BindView(R.id.mapView)
    MapView mapView;

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
        Timber.d("onCreate");
        setupViews();
        return view;
    }

    private void setupViews() {
        Timber.d("Setup view");
        presenter.setupMapView();
        presenter.setupFabView();
    }

    public void fabClick() {
        Timber.d("Find place");
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
        Timber.d("Multi Fab");
//        fab.inflate(R.menu.map_fab_menu);
//        fab.setOnActionSelectedListener(actionItem -> {
//            switch(actionItem.getId()) {
//                case R.id.fab_menu_search:
//                    findPlace();
//                    break;
//                case R.id.fab_menu_location:
//                    presenter.enableLocationComponent();
//                    break;
//            }
//            return false;
//        });

    }

    @Override
    public void setupMapBox() {
        Timber.d("Setup MapBox");
        mapView.getMapAsync(mapBoxMap -> mapBoxMap.setStyle(Style.DARK, style -> this.mapBoxMap = mapBoxMap));
    }

    @Override
    public void requestPermissions() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSION_REQUEST_CODE);
        Timber.d("Request permissions");
    }

    @Override
    @SuppressLint("MissingPermission")
    public void findUser() {
        if(PermissionsManager.areLocationPermissionsGranted(App.getInstance()) && mapBoxMap != null) {
            Timber.d("Find user");
            LocationComponent locationComponent = mapBoxMap.getLocationComponent();
            locationComponent.activateLocationComponent(App.getInstance(), Objects.requireNonNull(mapBoxMap.getStyle()));
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else if(! PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            Timber.d("Can't find user and request permissions");
            presenter.requestPermissions();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("Result query with find place");
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