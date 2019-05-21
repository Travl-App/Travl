package com.travl.guide.ui.fragment.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.android.core.permissions.PermissionsListener;
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
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.travl.guide.R;
import com.travl.guide.mvp.model.api.places.map.Place;
import com.travl.guide.mvp.presenter.maps.MapsPresenter;
import com.travl.guide.mvp.view.maps.MapsView;
import com.travl.guide.ui.App;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.travl.guide.ui.utils.MapUtils.MARKER_LAYER;
import static com.travl.guide.ui.utils.MapUtils.PLACES_GEO_SOURCE;
import static com.travl.guide.ui.utils.MapUtils.PLACE_IMAGE;
import static com.travl.guide.ui.utils.MapUtils.REQUEST_CODE_AUTOCOMPLETE;
import static com.travl.guide.ui.utils.MapUtils.SymbolGenerator;
import static com.travl.guide.ui.utils.MapUtils.convertToLatLng;

public class MapsFragment extends MvpAppCompatFragment implements MapsView, PermissionsListener {

    public static final String COORDS = "coords";
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.map_location_fab)
    FloatingActionButton locationFab;
    @InjectPresenter
    MapsPresenter presenter;


    private MapboxMap mapBoxMap;
    private List<Place> listPlaces;
    private List<Feature> markerCoordinates;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private GeoJsonSource geoJsonSource;

    public static MapsFragment getInstance(double[] coordinates) {
        MapsFragment instance = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putDoubleArray(COORDS, coordinates);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        FloatingActionButton fab = activity.findViewById(R.id.app_bar_fab);
        if (fab != null) {
            fab.setOnClickListener(v -> fabClick());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.e("OnCreateView MapsFragment");
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this, view);
        App.getInstance().getAppComponent().inject(this);
        Timber.e("mapView.onCreate");
        mapView.onCreate(savedInstanceState);
        Timber.e("SetupViews");
        setupViews();
        return view;
    }

    private void setupViews() {
        presenter.setupMapView();
        presenter.setupLocationFab();
    }

    @Override
    public void setupFab() {
        locationFab.setOnClickListener(view -> findUser());
    }

    @Override
    public void showLoadInfo() {

    }

    @Override
    public void hideLoadInfo() {

    }

    public void fabClick() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(getResources().getColor(R.color.transparent))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(getActivity());
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    @Override
    public void setupMapBox() {
        Timber.e("Setup MapBox");
        if (mapView != null) {
            mapView.getMapAsync(mapBoxMap -> {
                Timber.e("getMapAsync");
                this.mapBoxMap = mapBoxMap;
                mapBoxMap.setStyle(new Style.Builder().fromUrl(getString(R.string.mapbox_syle_link_minimo)), style -> {
                    Timber.e("Set Map style");
                    LocalizationPlugin localizationPlugin = new LocalizationPlugin(mapView, mapBoxMap, style);
                    localizationPlugin.matchMapLanguageWithDeviceDefault();
                    mapBoxMap.getUiSettings().setCompassEnabled(false);
                    mapBoxMap.getUiSettings().setLogoEnabled(false);
                    mapBoxMap.getUiSettings().setAttributionEnabled(false);
                    activateLocationComponent();
                    Timber.e("RequestForPlaces");
                    presenter.showUserLocation();
                    Bundle args = getArguments();
                    double[] coordinates = null;
                    if (args != null) {
                        coordinates = args.getDoubleArray(COORDS);
                    } else {
                        if (locationComponent != null) {
                            @SuppressLint("MissingPermission") Location location = locationComponent.getLastKnownLocation();
                            if (location != null) {
                                coordinates = new double[2];
                                coordinates[0] = location.getLatitude();
                                coordinates[1] = location.getLongitude();
                            }
                        }
                    }
                    presenter.makeRequestForPlaces(coordinates);
                });
            });
        }
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void onRequestCompleted(List<Place> listPlaces) {
        HashMap<Integer, View> viewMap = new HashMap<>();
        HashMap<String, Bitmap> bitmapMap = new HashMap<>();
        if (this.listPlaces == null) {
            this.listPlaces = listPlaces;
        } else {
            this.listPlaces.addAll(listPlaces);
        }

        for (int i = 0; i < listPlaces.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.map_mapillary_layout_callout, null);
            bitmapMap.put(listPlaces.get(i).getDescription(), SymbolGenerator(view));
            viewMap.put(listPlaces.get(i).getId(), view);
        }

        // if(mapBoxMap != null) mapBoxMap.getStyle().addImages(bitmapMap);
    }

    public void setupOnMapViewClickListener() {
        mapBoxMap.addOnMapClickListener(point -> {

            PointF screenPoint = mapBoxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapBoxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER);
            if (!features.isEmpty()) {

                Feature feature = features.get(0);
                LatLng coordinates = convertToLatLng(feature);
                PointF symbolScreenPoint = mapBoxMap.getProjection().toScreenLocation(coordinates);
                presenter.toPlaceScreen(feature.getNumberProperty("id").intValue());

            } /* else {
                onMarkerClickCallback(point.toString());
            } */
            return false;
        });
    }

    private void onMarkerClickCallback(String location) {
        Toast.makeText(getContext(), "Нажата точка c координатами: " + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlacesLoaded(List<Feature> markerCoordinates) {
        if (mapBoxMap != null) {
            Style style = mapBoxMap.getStyle();
            if (style != null) {
                SymbolLayer layer = new SymbolLayer(MARKER_LAYER, PLACES_GEO_SOURCE)
                        .withProperties(iconImage(PLACE_IMAGE),
                                iconOffset(new Float[]{0f, -9f}));
                if (this.markerCoordinates == null) {
                    this.markerCoordinates = markerCoordinates;
                } else {
                    this.markerCoordinates.addAll(markerCoordinates);
                }
                if (geoJsonSource == null) {
                    geoJsonSource = new GeoJsonSource(PLACES_GEO_SOURCE, FeatureCollection.fromFeatures(this.markerCoordinates));
                    style.addSource(geoJsonSource);
                    style.addImage(PLACE_IMAGE, getResources().getDrawable(R.drawable.ic_place_black));
                    style.addLayer(layer);
                    setupOnMapViewClickListener();
                } else {
                    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(this.markerCoordinates));
                }
            }
        }
    }

    @Override
    public void showUserLocation() {
        Bundle args = getArguments();
        if (args != null) {
            double[] coordinates = args.getDoubleArray(COORDS);
            if (coordinates != null) {
                Timber.e("Moving camera to coordinates " + Arrays.toString(coordinates));
                moveCameraToCoordinates(coordinates);
                return;
            }
        }
        Timber.e("Args = " + args);
        findUser();
    }

    @SuppressLint("MissingPermission")
    public void findUser() {
        if (PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            if (mapBoxMap.getStyle() != null) {
                locationComponent = mapBoxMap.getLocationComponent();
                locationComponent.activateLocationComponent(App.getInstance(), mapBoxMap.getStyle());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.NORMAL);
            }
        } else if (!PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @SuppressLint("MissingPermission")
    private void activateLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            if (mapBoxMap.getStyle() != null) {
                locationComponent = mapBoxMap.getLocationComponent();
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.activateLocationComponent(App.getInstance(), mapBoxMap.getStyle());
            }
        } else if (!PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("Result query with find place");
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            if (mapBoxMap != null) {
                Style style = mapBoxMap.getStyle();
                if (style != null) {
                    String geoJsonSourceLayerId = "geoJsonSourceLayerId";
                    GeoJsonSource source = style.getSourceAs(geoJsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "This app needs location permissions in order to show its functionality.", Toast.LENGTH_SHORT).show();
    }

    private void moveCameraToCoordinates(double[] coordinates) {
        LatLng latLng = new LatLng();
        latLng.setLatitude(coordinates[0]);
        latLng.setLongitude(coordinates[1]);
        mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(latLng)
                .zoom(9)
                .build()));
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            presenter.showUserLocation();
        } else {
            Toast.makeText(getContext(), "You didn\'t grant location permissions.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        if (locationComponent != null) {
            locationComponent.onStart();
        }
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
        if (locationComponent != null) {
            locationComponent.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        if (locationComponent != null) {
            locationComponent.onDestroy();
        }
        presenter.onDispose();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}