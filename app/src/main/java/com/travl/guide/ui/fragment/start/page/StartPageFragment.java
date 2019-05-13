package com.travl.guide.ui.fragment.start.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.mvp.model.location.LocationReceiver;
import com.travl.guide.mvp.model.network.CoordinatesRequest;
import com.travl.guide.mvp.model.user.User;
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.CoordinatesProvider;
import com.travl.guide.ui.fragment.articles.city.CityArticlesFragment;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.travl.guide.util.UtilVariables.COARSE_LOCATION_PERMISSION;
import static com.travl.guide.util.UtilVariables.FINE_LOCATION_PERMISSION;
import static com.travl.guide.util.UtilVariables.LOCATION_PERMISSIONS_REQUEST_CODE;

public class StartPageFragment extends MvpAppCompatFragment implements StartPageView, LocationReceiver, CoordinatesProvider {

    public static final String CITY_ARTICLES_FRAGMENT_TAG = "ArticlesFragment";
    public static final int CODE_OK = 200;
    public static final int CODE_ERROR = 404;
    @InjectPresenter
    StartPagePresenter presenter;
    @BindView(R.id.user_city_spinner)
    Spinner userCitySpinner;
    @BindView(R.id.start_page_toolbar)
    Toolbar toolbar;
    private City currentCity;
    private String selectedCity;
    private CityContent cityContent;
    private CitiesList cityObjectList;
    //List of formatted String city names
    private List<String> cityStringNames;
    //TODO Перенести в модель логику формирования и парсинга списка городов
    private CitySpinnerListCreator listCreator;
    //Array of city names shown in spinner
    private ArrayAdapter<String> cityArrayAdapter;
    private ArticlesReceiver articlesReceiver;
    private double[] citySelectedCoordinates;

    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_page_fragment, container, false);
        ButterKnife.bind(this, view);
        presenter.initLocationListener();
        presenter.requestCoordinates(this);
        listCreator = new CitySpinnerListCreator();
        if (savedInstanceState == null) {
            initCityArticlesFragment();
            initTravlZineFragment();
        }
        initCitySpinner();
        presenter.loadCitiesList();
        decideCityFragmentContainerTitleVisibility();
        return view;
    }

    public void initCitySpinner() {
        initCitySpinnerList();
        initCitySpinnerClickListener();
    }

    private void setCitySelectedName(String citySelected) {
        this.selectedCity = citySelected;
    }

    private void initCitySpinnerList() {
        initCityArrayAdapter();
        addCityArrayAdapterToSpinner();
    }


    public void initCityArrayAdapter() {
        cityArrayAdapter = new ArrayAdapter<>(App.getInstance(), R.layout.start_page_cities_spinner_item);
        cityArrayAdapter.addAll(getResources().getStringArray(R.array.cities));
    }


    public void addCityArrayAdapterToSpinner() {
        userCitySpinner.setAdapter(cityArrayAdapter);
    }

    @Override
    public void setCityStringNames(ArrayList<String> cityStringNames) {
        this.cityStringNames = cityStringNames;
    }

    @Override
    public void onSpinnerItemClick(String selectedCity) {
        Timber.e("OnSpinnerClick");
        if (cityStringNames != null && cityObjectList != null) {
            if (cityStringNames.contains(selectedCity)) {
                for (CitiesList.CityLink link : cityObjectList.getCities()) {
                    String formattedLink = listCreator.formatCityLink(link);
                    if (selectedCity.equals(formattedLink) || (getString(R.string.user_location_marker) + " " + selectedCity).equals(formattedLink)) {
                        presenter.loadCityContentByLinkId(link.getId());
                        return;
                    }
                }
            } else {
                Timber.e("City is not in the object list");
                User user = User.getInstance();
                String userCityName = user.getCityName();
                if (userCityName != null) {
                    if (selectedCity.contains(userCityName)) {
                        Timber.e("City is User's");
                        presenter.loadCityContentByCoordinates(new CoordinatesRequest(user.getCoordinates()));
                    }
                }
            }
        }
    }

    @Override
    public void addToCityList(City city, boolean isUserCity) {
        listCreator.addToCityList(city, cityArrayAdapter, presenter, isUserCity);
    }

    private void initCitySpinnerClickListener() {
        userCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String spinnerCityName = (String) adapterView.getItemAtPosition(position);
                Timber.e("onItemSelected = " + spinnerCityName);
                if (selectedCity == null || !selectedCity.equals(spinnerCityName)) {
                    setCitySelectedName(spinnerCityName);
                }
                presenter.onSpinnerItemClick(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    @Override
    public void removePlaceIfIsAdded(String placeName) {
        cityArrayAdapter.remove(placeName);
        cityArrayAdapter.remove(getString(R.string.user_location_marker) + " " + placeName);
        cityArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSpinnerPositionSelected(int position) {
        userCitySpinner.setSelection(position, false);
    }

    @Override
    public void placeSelectedCityOnTop(String placeName) {
        Timber.e("Placing on top = " + placeName);
        if (userCitySpinner != null && cityArrayAdapter != null && userCitySpinner.getCount() > 0) {
            if (!userCitySpinner.getItemAtPosition(0).equals(placeName)) {
                Timber.e("Item at position 0 does not equal to " + placeName + ". Replacing...");
                cityArrayAdapter.insert(placeName, 0);
                presenter.setSpinnerPositionSelected(0);
                cityArrayAdapter.notifyDataSetChanged();
            }
        }
    }


    public void initTravlZineFragment() {
        Fragment travlZineArticlesFragment = new TravlZineArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_travl_zine_container, travlZineArticlesFragment).commit();
    }


    public void initCityArticlesFragment() {
        Fragment cityArticlesFragment = new CityArticlesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_city_articles_container, cityArticlesFragment, CITY_ARTICLES_FRAGMENT_TAG).commit();
    }

    @Override
    public void setCityObjectList(CitiesList cityObjectList) {
        this.cityObjectList = cityObjectList;
    }

    @Override
    public void transformCityObjectsToCityStrings() {
        cityStringNames = listCreator.citiesListToCitiesNameList(cityObjectList);
    }

    @Override
    public void addNamesToCitySpinner() {
        if (this.cityStringNames != null) {
            if (cityArrayAdapter != null) {
                cityArrayAdapter.clear();
                for (int i = 0; i < cityStringNames.size(); i++) {
                    cityArrayAdapter.add(cityStringNames.get(i));
                }
                cityArrayAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void setCityContentByCoordinates(CityContent cityContent) {
        Timber.e("SetByCoordinates");
        this.cityContent = cityContent;
        hideCityArticlesFragment();
        presenter.setCurrentCity(cityContent);
        presenter.addToCityList(currentCity, true);
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(currentCity));
        setCitySelectedName(cityName);
        User.getInstance().setCityName(cityName);
        presenter.setCityArcticles();
    }

    private void hideCityArticlesFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.start_page_city_articles_container);
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragment).commit();
            setContainerTitleVisibility(View.GONE);
        }
    }

    private void showCityArticlesFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.start_page_city_articles_container);
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(fragment).commit();
            setContainerTitleVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCityContentByLinkId(CityContent cityContent) {
        Timber.e("SetById");
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(cityContent.getCity()));
        //If no city is selected or loaded and if the info is related to the city selected
        if (selectedCity == null || cityName != null && (cityName.equals(selectedCity)
                || cityName.equals(getString(R.string.user_location_marker) + " " + selectedCity))) {
            this.cityContent = cityContent;
            hideCityArticlesFragment();
            presenter.setCurrentCity(cityContent);
            removePlaceIfIsAdded(cityName);
            placeSelectedCityOnTop(cityName);
            presenter.setCityArcticles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestLocationPermissions() {
        Timber.e("Request permissions");
        Activity activity = getActivity();
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{COARSE_LOCATION_PERMISSION, FINE_LOCATION_PERMISSION}, LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Activity activity = getActivity();
        if (activity != null) {
            Snackbar.make(activity.findViewById(R.id.fragment_container), "We need GPS to show your city specific content", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationPermissionRequestGranted() {
        presenter.requestCoordinates(this);
    }

    @Override
    public void setCurrentCity(CityContent cityContent) {
        if (cityContent != null) {
            int status = cityContent.getStatus();
            if (status == CODE_OK) {
                currentCity = cityContent.getCity();
                citySelectedCoordinates = currentCity.getCoordinates();
                Timber.e("coords = " + Arrays.toString(citySelectedCoordinates));
            } else if (status == CODE_ERROR) {
                currentCity = cityContent.getContext();
                citySelectedCoordinates = new double[]{currentCity.getLatitude(), currentCity.getLongitude()};
                Timber.e("coords = " + Arrays.toString(citySelectedCoordinates));
            }
        }
    }

    @Override
    public double[] getCoordinates() {
        return citySelectedCoordinates;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        String tag = childFragment.getTag();
        if (tag != null && tag.equals(CITY_ARTICLES_FRAGMENT_TAG)) {
            articlesReceiver = (ArticlesReceiver) childFragment;
            if (presenter != null) {
                presenter.setCityArcticles();
            }
        }
    }

    @Override
    public void setCityArticles() {
        if (articlesReceiver != null
                && currentCity != null
                && currentCity.getArticleLinksContainer() != null) {
            Timber.e("articleLinksContainer not null");
            articlesReceiver.setArticles(currentCity.getArticleLinksContainer().getArticleLinkList());
            showCityArticlesFragment();
            decideCityFragmentContainerTitleVisibility();
        }
    }

    @Override
    public void onLocationPermissionResult(boolean granted) {
        presenter.onLocationPermissionResult(granted);
    }

    public void decideCityFragmentContainerTitleVisibility() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.start_page_city_articles_container);
        if (fragment != null) {
            setContainerTitleVisibility(View.VISIBLE);
        } else {
            if (articlesReceiver != null) {
                if (articlesReceiver.getArticlesNumber() > 0) {
                    setContainerTitleVisibility(View.VISIBLE);
                } else {
                    setContainerTitleVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        decideCityFragmentContainerTitleVisibility();
    }

    private void setContainerTitleVisibility(int visibility) {
        Activity activity = getActivity();
        if (activity != null) {
            TextView titleTextView = activity.findViewById(R.id.start_page_city_articles_container_title);
            if (titleTextView != null) {
                titleTextView.setVisibility(visibility);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDispose();
    }

    public interface ArticlesReceiver {
        void setArticles(List<ArticleLink> articleLinks);

        int getArticlesNumber();
    }
}
