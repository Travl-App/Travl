package com.travl.guide.ui.fragment.start.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.travl.guide.R;
import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.fragment.articles.city.CityArticlesFragment;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class StartPageFragment extends MvpAppCompatFragment implements StartPageView, PermissionsListener {

    public static final String CITY_ARTICLES_FRAGMENT_TAG = "ArticlesFragment";
    public static final int LOCATION_PERMISSIONS_REQUEST_CODE = 0;
    private static final String COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    @BindView(R.id.user_city_spinner)
    Spinner userCitySpinner;
    @BindView(R.id.start_page_toolbar)
    Toolbar toolbar;
    @InjectPresenter
    StartPagePresenter presenter;
    private ArrayAdapter<String> cityArrayAdapter;
    private LocationManager locationManager;
    private CitiesList cityObjectList;
    private City city;
    private String selectedCity;
    private List<String> cityStringNames;
    private CityContent cityContent;
    private CitySpinnerListCreator listCreator;
    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Timber.e("Location changed:" + location.getLatitude() + "," + location.getLongitude());
            setCoordinates(location);
            presenter.loadCityContentByCoordinates(User.getInstance().getCoordinates());
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
    private ArticlesReceiver articlesReceiver;

    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_page_fragment, container, false);
        ButterKnife.bind(this, view);
        locationManager = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        listCreator = new CitySpinnerListCreator();
        if (savedInstanceState == null) {
            //Timber.e("SavedInstance null");
            presenter.initCityArticlesFragment();
            presenter.initTravlZineArticlesFragment();
            presenter.initCitySpinner();
        }
        presenter.requestCoordinates();
        presenter.loadCityContentByCoordinates(User.getInstance().getCoordinates());
        presenter.loadCitiesList();

        return view;
    }

    @Override
    public void initCitySpinner() {
        initCitySpinnerList();
        initCitySpinnerClickListener();
    }

    @Override
    public void setCitySelectedName(String citySelected) {
        Timber.e("setCitySelectedName " + citySelected);
        this.selectedCity = citySelected;
    }

    private void initCitySpinnerList() {
        presenter.setCityArrayAdapter();
        presenter.setCityStringNames(listCreator.citiesListToCitiesNameList(cityObjectList));
        if (this.cityStringNames == null)
            this.cityStringNames = Arrays.asList(getResources().getStringArray(R.array.cities));
        presenter.addNamesToCitySpinner(cityStringNames);
        presenter.addCityArrayAdapterToSpinner();
    }

    @Override
    public void setCityArrayAdapter() {
        Timber.e("setCityArrayAdapter");
        cityArrayAdapter = new ArrayAdapter<>(App.getInstance(), R.layout.cities_spinner_item);
    }

    @Override
    public void addCityArrayAdapterToSpinner() {
        Timber.e("addCityArrayAdapterToSpinner");
        userCitySpinner.setAdapter(cityArrayAdapter);
    }

    @Override
    public void setCityStringNames(ArrayList<String> cityStringNames) {
        Timber.e("setCityStringNames " + Arrays.toString(cityStringNames.toArray()));
        this.cityStringNames = cityStringNames;
    }

    @Override
    public void onSpinnerItemClick(String selectedCity) {
        Timber.e("OnItemSelected CityNames =" + cityStringNames + " \n City = " + selectedCity + "\n CitiesList = " + cityObjectList);
        if (cityStringNames != null) {
            if (cityObjectList != null && cityStringNames.contains(selectedCity)) {
                for (CitiesList.CityLink link : cityObjectList.getCities()) {
                    if (selectedCity.equals(listCreator.formatCityLink(link))) {
                        Timber.e("Loading city by id");
                        presenter.loadCityContentByLinkId(link.getId());
                    }
                }
            } else {
                presenter.loadCityContentByCoordinates(User.getInstance().getCoordinates());
            }
        }
    }

    @Override
    public void addToCityList(City city, boolean isUserCity) {
        Timber.e("Editing city list");
        listCreator.addToCityList(city, cityArrayAdapter, presenter, isUserCity);
    }

    private void initCitySpinnerClickListener() {
        userCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String spinnerCityName = (String) adapterView.getItemAtPosition(position);
                if (selectedCity != null && selectedCity.equals(spinnerCityName))
                    return;
                presenter.setCitySelectedName(spinnerCityName);
                presenter.onSpinnerItemClick(selectedCity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void removePlaceIfIsAdded(String placeName) {
        Timber.e("Removing " + placeName);
        cityArrayAdapter.remove(placeName);
    }

    @Override
    public void setSpinnerPositionSelected(int position) {
        Timber.e("setSpinner selection position " + position);
        userCitySpinner.setSelection(position, false);
    }

    @Override
    public void placeSelectedCityOnTop(String placeName) {
        Timber.e("Inserting " + placeName);
        if (userCitySpinner != null && cityArrayAdapter != null && userCitySpinner.getCount() > 0) {
            if (!userCitySpinner.getItemAtPosition(0).equals(placeName)) {
                cityArrayAdapter.insert(placeName, 0);
                presenter.setSpinnerPositionSelected(0);
            }
        }
    }

    private void setCoordinates(Location lastKnownLocation) {
        if (lastKnownLocation != null) {
            presenter.setUserCoordinates(new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()});
        }
    }

    @Override
    public void initTravlZineFragment() {
        Timber.e("initTravlZineFragment");
        Fragment travlZineArticlesFragment = new TravlZineArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_travl_zine_container, travlZineArticlesFragment).commit();
    }

    @Override
    public void initCityArticlesFragment() {
        Timber.e("initCityArticlesFragment");
        Fragment cityArticlesFragment = new CityArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_city_articles_container, cityArticlesFragment, CITY_ARTICLES_FRAGMENT_TAG).commit();
    }

    @Override
    public void setCityObjectList(CitiesList cityObjectList) {
        Timber.e("setCityObjectList");
        this.cityObjectList = cityObjectList;
        cityStringNames = listCreator.citiesListToCitiesNameList(cityObjectList);
        presenter.addNamesToCitySpinner(cityStringNames);
    }

    @Override
    public void addNamesToCitySpinner(List<String> cityStringNames) {
        Timber.e("addNamesToCitySpinner" + cityStringNames);
        for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
            String item = cityArrayAdapter.getItem(i);
            cityStringNames.remove(item);
        }
        cityArrayAdapter.addAll(cityStringNames);
    }


    @Override
    public void setCityContentByCoordinates(CityContent cityContent) {
        this.cityContent = cityContent;

        //If no city is selected or loaded and if the info is related to the city selected
        presenter.setCity(cityContent);
        presenter.addToCityList(city, true);
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(city));
        presenter.setCitySelectedName(cityName);
        if (articlesReceiver != null && this.cityContent != null && city != null) {
            presenter.setCityArcticles();
        }
    }

    @Override
    public void setCityContentByLinkId(CityContent cityContent) {
        Timber.e("setCityContent");
        this.cityContent = cityContent;
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(cityContent.getCity()));
        //If no city is selected or loaded and if the info is related to the city selected
        if (selectedCity == null || cityName != null && cityName.equals(selectedCity)) {
            presenter.setCity(cityContent);
            presenter.addToCityList(city, false);
            if (articlesReceiver != null && this.cityContent != null && city != null) {
                presenter.setCityArcticles();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.e("onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            onPermissionResult(granted);
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestCoordinates() {
        if (ContextCompat.checkSelfPermission(App.getInstance(), COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else if (!(ContextCompat.checkSelfPermission(App.getInstance(), COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED)) {
            Activity activity = getActivity();
            if (activity != null) {
                ActivityCompat.requestPermissions(activity, new String[]{COARSE_LOCATION_PERMISSION}, LOCATION_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.fragment_container), "We need GPS to show your city specific content", Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void setCity(CityContent cityContent) {
        Timber.e("setCity");
        if (cityContent != null) {
            int status = cityContent.getStatus();
            if (status == 200) {
                city = cityContent.getCity();
            } else if (status == 404) {
                city = cityContent.getContext();
            }
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        String tag = childFragment.getTag();
        Timber.e("OnAttachFragment tag = " + tag);
        if (tag != null && tag.equals(CITY_ARTICLES_FRAGMENT_TAG)) {
            articlesReceiver = (ArticlesReceiver) childFragment;
            if (presenter != null) {
                presenter.setCityArcticles();
            }
        }
    }

    @Override
    public void setCityArticles() {
        Timber.e("setCityArticles");
        if (city != null) {
            presenter.decideCityArticlesTitleVisibility();
            articlesReceiver.setArticles(city.getArticleLinks());
        }
    }


    @Override
    public void decideCityArticlesTitleVisibility() {
        if (city != null) {
            List<ArticleLink> links = city.getArticleLinks();
            Activity activity = getActivity();
            int visibility = View.GONE;
            if (links != null && links.size() > 0) {
                visibility = View.VISIBLE;
            }

            if (activity != null)
                activity.findViewById(R.id.start_page_city_articles_container_title).setVisibility(visibility);
        }
    }

    @Override
    public void onPermissionResult(boolean granted) {
        Timber.e("onPermissionResult " + granted);
        if (granted) {
            presenter.requestCoordinates();
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestLocation() {
        Timber.e("requestLocation");
        int millisInSecond = 1000;
        int minutes = 1;
        int secondsInMinutes = 30;
        int meters = 100;
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, minutes * secondsInMinutes * millisInSecond, meters, mListener);
    }

    public interface ArticlesReceiver {
        void setArticles(List<ArticleLink> articleLinks);
    }
}
