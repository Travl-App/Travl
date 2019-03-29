package com.travl.guide.ui.fragment.start.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
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
import com.travl.guide.mvp.presenter.StartPagePresenter;
import com.travl.guide.mvp.view.StartPageView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.fragment.places.ArticlesFragment;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class StartPageFragment extends MvpAppCompatFragment implements StartPageView, PermissionsListener {


    //    @BindView(R.id.city_image_view)
//    ImageView cityImageView;
    @BindView(R.id.user_city_spinner)
    Spinner userCitySpinner;
    @BindView(R.id.start_page_toolbar)
    Toolbar toolbar;
    @InjectPresenter
    StartPagePresenter presenter;
    private ArrayAdapter<String> cityArrayAdapter;
    private PermissionsManager permissionsManager;

    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_page_fragment, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            presenter.initPlacesFragment();
        }
        requestCoordinates();
        presenter.loadCityContent(User.getInstance().getCoordinates());
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
        return view;
    }

    @SuppressLint("MissingPermission")
    public void requestCoordinates() {
        LocationManager locationManager = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            setCoordinates(lastKnownLocation);
        } else if (!PermissionsManager.areLocationPermissionsGranted(App.getInstance())) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    private void setCoordinates(Location lastKnownLocation) {
        if (lastKnownLocation != null) {
            Timber.e("Setting coordinates to " + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
            User.getInstance().setCoordinates(new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()});
        }
    }

    @Override
    public void initArticlesFragment() {
        Fragment articlesFragment = new ArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_articles_container, articlesFragment).commit();
    }

    @Override
    public void setCityName(String placeName) {
        boolean isPlaceAdded = false;
        for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
            if (((String) Objects.requireNonNull(cityArrayAdapter.getItem(i))).equals(placeName)) {
                isPlaceAdded = true;
            }
        }
        if (!isPlaceAdded) {
            cityArrayAdapter.insert(placeName, 0);
        }
        // cityNameTextView.setText(placeName);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.fragment_container), "We need GPS to show your city specific content", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            requestCoordinates();
        }
    }
}
