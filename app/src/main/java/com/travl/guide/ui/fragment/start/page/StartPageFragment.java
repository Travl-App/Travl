package com.travl.guide.ui.fragment.start.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.mvp.view.start.page.StartPageView;
import com.travl.guide.navigator.CurrentScreen;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.CoordinatesProvider;
import com.travl.guide.ui.activity.OnMoveToNavigator;
import com.travl.guide.ui.fragment.articles.city.CityArticlesFragment;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;

import java.util.ArrayList;
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
    @BindView(R.id.start_page_city_loading)
    TextView cityLoadingTextView;
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
    private OnMoveToNavigator moveToNavigator;
    private int selectedCityId;

    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_page_fragment, container, false);
        ButterKnife.bind(this, view);
        listCreator = new CitySpinnerListCreator();
        if (savedInstanceState == null) {
            initCityArticlesFragment();
            initTravlZineFragment();
        }
        return view;
    }

    public void initCitySpinner() {
        initCitySpinnerList();
        initCitySpinnerClickListener();
        initCityInfoButton();
    }

    private void initCityInfoButton() {
        Activity activity = getActivity();
        if (activity != null) {
            ImageView imageView = activity.findViewById(R.id.start_page_city_info_button);
            if (imageView != null) {
                imageView.setOnClickListener(v -> {
                    presenter.OnCityInfoButtonClick(selectedCityId);
                });
            }
        }
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
        if (cityStringNames != null && cityObjectList != null) {
            if (cityStringNames.contains(selectedCity)) {
                for (CitiesList.CityLink link : cityObjectList.getCities()) {
                    String formattedLink = listCreator.formatCityLink(link);
                    if (selectedCity.equals(formattedLink) || (getString(R.string.user_location_marker) + " " + selectedCity).equals(formattedLink)) {
                        selectedCityId = link.getId();
                        presenter.loadCityContentByLinkId(link.getId());
                        return;
                    }
                }
            } else {
                String userCityName = presenter.getCityName();
                if (userCityName != null) {
                    if (selectedCity.contains(userCityName)) {
                        presenter.loadCityContentByCoordinates();
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
        if (userCitySpinner != null && cityArrayAdapter != null && userCitySpinner.getCount() > 0) {
            if (!userCitySpinner.getItemAtPosition(0).equals(placeName)) {
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        presenter.initLocationListener();
        presenter.requestCoordinates(this);
        initCitySpinner();
        presenter.loadCitiesList();
        if (moveToNavigator != null) {
            moveToNavigator.onMoveTo(CurrentScreen.INSTANCE.start());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoveToNavigator) {
            moveToNavigator = (OnMoveToNavigator) context;
        }
    }

    @Override
    public void setCityContentByCoordinates(CityContent cityContent) {
        this.cityContent = cityContent;
        hideCityArticlesFragment();
        presenter.setCurrentCity(cityContent);
        presenter.addToCityList(currentCity, true);
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(currentCity));
        setCitySelectedName(cityName);
        presenter.setCityName(cityName);
        presenter.setCityArcticles();
        showCitiesList();
    }

    private void showCitiesList() {
        cityLoadingTextView.setVisibility(View.GONE);
        userCitySpinner.setVisibility(View.VISIBLE);
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
        }
    }

    @Override
    public void setCityContentByLinkId(CityContent cityContent) {
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
        Activity activity = getActivity();
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{COARSE_LOCATION_PERMISSION, FINE_LOCATION_PERMISSION}, LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Activity activity = getActivity();
        if (activity != null) {
            Snackbar.make(activity.findViewById(R.id.container), "We need GPS to show your city specific content", Snackbar.LENGTH_LONG).show();
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
            } else if (status == CODE_ERROR) {
                currentCity = cityContent.getContext();
                citySelectedCoordinates = new double[]{currentCity.getLatitude(), currentCity.getLongitude()};
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
            articlesReceiver.setArticles(currentCity.getArticleLinksContainer().getArticleLinkList());
            showCityArticlesFragment();
        }
    }

    @Override
    public void onLocationPermissionResult(boolean granted) {
        presenter.onLocationPermissionResult(granted);
    }


    private void setContainerTitleVisibility(int visibility) {
        Timber.e("set visibility to " + visibility);
        Activity activity = getActivity();
        if (activity != null) {
            TextView titleTextView = activity.findViewById(R.id.start_page_city_articles_container_title);
            if (titleTextView != null) {
                titleTextView.setVisibility(visibility);
            }
            ImageView listsSeparator = activity.findViewById(R.id.start_page_lists_separator);
            if (listsSeparator != null) {
                listsSeparator.setVisibility(visibility);
            }
            FrameLayout cityArticlesContainer = activity.findViewById(R.id.start_page_city_articles_container);
            if (cityArticlesContainer != null) {
                cityArticlesContainer.setVisibility(visibility);
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
