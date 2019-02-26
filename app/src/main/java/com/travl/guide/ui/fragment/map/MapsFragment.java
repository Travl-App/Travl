package com.travl.guide.ui.fragment.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.MapsPresenter;
import com.travl.guide.mvp.view.MapsView;
import com.travl.guide.ui.App;

import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

//Created by Pereved on 21.02.2019.
public class MapsFragment extends MvpAppCompatFragment implements MapsView {

    private static MapsFragment fragment = new MapsFragment();

    @Singleton
    public static MapsFragment getInstance() {
        return fragment;
    }

    @InjectPresenter
    MapsPresenter presenter;
    @BindView(R.id.mapView)
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this, view);
        App.getInstance().getAppComponent().inject(this);
        setupMapBox();
        return view;
    }

    //TODO: Костыль №1
    private void setupMapBox() {
        mapView.getMapAsync(mapBoxMap -> mapBoxMap.setStyle(Style.DARK, style -> {
            enableLocationComponent(mapBoxMap);
        }));
    }

    //TODO: Костыль №2
    @SuppressLint("MissingPermission")
    private void enableLocationComponent(MapboxMap mapboxMap) {
        if(PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(App.getInstance(), mapboxMap.getStyle());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
//            PermissionsManager permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(getActivity());
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