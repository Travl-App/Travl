package com.travl.guide.ui.fragment.start.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.travl.guide.R;
import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.fragment.articles.city.CityArticlesFragment;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class StartPageFragment extends MvpAppCompatFragment implements StartPageView, PermissionsListener {

    @BindView(R.id.user_city_spinner)
    Spinner userCitySpinner;
    @BindView(R.id.start_page_toolbar)
    Toolbar toolbar;
    @InjectPresenter
    StartPagePresenter presenter;
    private ArrayAdapter<String> cityArrayAdapter;
    private LocationManager locationManager;

    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Timber.e("Location changed:" + location.getLatitude() + "," + location.getLongitude());
            setCoordinates(location);
            presenter.loadCityContent(User.getInstance().getCoordinates());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_page_fragment, container, false);
        ButterKnife.bind(this, view);
        locationManager = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        if (savedInstanceState == null) {
            presenter.initCityArticlesFragment();
            presenter.initTravlZineArticlesFragment();
        }
        requestCoordinates();
        presenter.loadCityContent(User.getInstance().getCoordinates());
        initCitySpinner(view);
        return view;
    }

    private void initCitySpinner(View view) {
        cityArrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.cities_spinner_item);
        cityArrayAdapter.addAll(getResources().getStringArray(R.array.cities));
        userCitySpinner.setAdapter(cityArrayAdapter);
        userCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Санкт-Петербу́рг")) {
                    presenter.loadCityContent(new double[]{59.93, 30.33});
                } else {
                    presenter.loadCityContent(User.getInstance().getCoordinates());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCoordinates(Location lastKnownLocation) {
        if (lastKnownLocation != null) {
            Timber.e("Setting coordinates to " + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
            User.getInstance().setCoordinates(new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()});
        }
    }

    @Override
    public void initArticlesFragment() {
        Fragment travlZineArticlesFragment = new TravlZineArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_travl_zine_container, travlZineArticlesFragment).commit();
    }

    @Override
    public void initCityArticlesFragment() {
        Fragment cityArticlesFragment = new CityArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_city_articles_container, cityArticlesFragment).commit();
    }

    @SuppressLint("MissingPermission")
    public void requestCoordinates() {
        if (PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            requestLocation();
        } else if (!PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.fragment_container), "We need GPS to show your city specific content", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setCityName(String placeName) {
        if (!placeName.equals("Не найден")) {
            boolean isPlaceAdded = false;
            for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
                if (((String) Objects.requireNonNull(cityArrayAdapter.getItem(i))).equals(placeName)) {
                    isPlaceAdded = true;
                }
            }
            if (!isPlaceAdded) {
                cityArrayAdapter.insert(placeName, 0);
            }
        }
    }



    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            requestLocation();
            requestCoordinates();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        int millisInSecond = 1000;
        int minutes = 5;
        int secondsInMinutes = 60;
        int meters = 100;
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, minutes * secondsInMinutes * millisInSecond, meters, mListener);
    }
}
