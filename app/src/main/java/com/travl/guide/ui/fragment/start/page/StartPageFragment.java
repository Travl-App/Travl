package com.travl.guide.ui.fragment.start.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
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
    private List<String> cityStringNames;
    private CityContent cityContent;
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
        if (savedInstanceState == null) {
            presenter.initCityArticlesFragment();
            presenter.initTravlZineArticlesFragment();
            presenter.requestCoordinates();
            presenter.loadCityContentByCoordinates(User.getInstance().getCoordinates());
        }
        presenter.loadCitiesList();

        initCitySpinner(view);
        return view;
    }

    private void initCitySpinner(View view) {
        initCitySpinnerList(view);
        initCitySpinnerClickListener();
    }

    private void initCitySpinnerList(View view) {
        cityArrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.cities_spinner_item);
        presenter.setCityNames(CitySpinnerListCreator.citiesListToCitiesNameList(cityObjectList));
        if (cityStringNames == null)
            cityStringNames = Arrays.asList(getResources().getStringArray(R.array.cities));
        cityArrayAdapter.addAll(cityStringNames);
        userCitySpinner.setAdapter(cityArrayAdapter);
    }

    @Override
    public void setCityStringNames(ArrayList<String> cityStringNames) {
        this.cityStringNames = cityStringNames;
    }

    @Override
    public void onSpinnerItemClick(String selectedCity) {
        Timber.e("OnItemSelected CityNames =" + cityStringNames + " \n City = " + selectedCity + "\n CitiesList = " + cityObjectList);
        if (cityStringNames != null) {
            if (cityObjectList != null && cityStringNames.contains(selectedCity)) {
                for (CitiesList.CityLink link : cityObjectList.getCities()) {
                    if (selectedCity.equals(CitySpinnerListCreator.formatCityLink(link))) {
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
    public void editCityList(City city) {
        CitySpinnerListCreator.editCityList(city, cityArrayAdapter, userCitySpinner);
    }

    private void initCitySpinnerClickListener() {
        userCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = (String) adapterView.getItemAtPosition(i);
                presenter.onSpinnerItemClick(selectedCity);
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
        transaction.replace(R.id.start_page_city_articles_container, cityArticlesFragment, CITY_ARTICLES_FRAGMENT_TAG).commit();
    }

    @Override
    public void setCityObjectList(CitiesList cityObjectList) {
        this.cityObjectList = cityObjectList;
        cityStringNames = CitySpinnerListCreator.citiesListToCitiesNameList(cityObjectList);
        for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
            String name = cityArrayAdapter.getItem(i);
            if (cityStringNames.contains(name)) {
                cityArrayAdapter.remove(name);
            }
        }
        cityArrayAdapter.addAll(cityStringNames);
    }

    @Override
    public void setCityContentByLinkId(CityContent cityContent) {
        setCityContent(cityContent);
    }

    @Override
    public void setCityContentByCoordinates(CityContent cityContent) {
        setCityContent(cityContent);
    }

    private void setCityContent(CityContent cityContent) {
        this.cityContent = cityContent;
        presenter.setCityName(cityContent);
        presenter.editCityList(city);
        if (articlesReceiver != null && this.cityContent != null && city != null) {
            areThereArticles();
            articlesReceiver.setArticles(city.getArticleLinks());
        }
    }

    @Override
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
    public void setCityName(CityContent cityContent) {
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
            if (city != null) {
                areThereArticles();
                articlesReceiver.setArticles(city.getArticleLinks());
            }
        }
    }

    private void areThereArticles() {
        List<ArticleLink> links = city.getArticleLinks();
        Activity activity = getActivity();
        int visibility = View.GONE;
        if (links != null && links.size() > 0) {
            visibility = View.VISIBLE;
        }

        if (activity != null)
            activity.findViewById(R.id.start_page_city_articles_container_title).setVisibility(visibility);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            presenter.requestLocation();
            presenter.requestCoordinates();
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestLocation() {
        int millisInSecond = 1000;
        int minutes = 5;
        int secondsInMinutes = 60;
        int meters = 100;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(
                provider, minutes * secondsInMinutes * millisInSecond, meters, mListener);
    }

    public interface ArticlesReceiver {
        void setArticles(List<ArticleLink> articleLinks);
    }
}
