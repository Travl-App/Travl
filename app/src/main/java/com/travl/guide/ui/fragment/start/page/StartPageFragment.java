package com.travl.guide.ui.fragment.start.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
    public static final int CODE_OK = 200;
    public static final int CODE_ERROR = 404;
    @InjectPresenter
    StartPagePresenter presenter;
    @BindView(R.id.user_city_spinner)
    Spinner userCitySpinner;
    @BindView(R.id.start_page_toolbar)
    Toolbar toolbar;
    private City city;
    private String selectedCity;
    private CityContent cityContent;
    private CitiesList cityObjectList;
    private List<String> cityStringNames;
    private CitySpinnerListCreator listCreator;
    private ArrayAdapter<String> cityArrayAdapter;
    private StartPageLocationManager startPageLocationManager;
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
        startPageLocationManager = new StartPageLocationManager(presenter);
        startPageLocationManager.initLocationListener();
        listCreator = new CitySpinnerListCreator();
        if (savedInstanceState == null) {
            initCityArticlesFragment();
            initTravlZineFragment();
        }
        initCitySpinner();
        decideCityFragmentContainerTitleVisibility();
        presenter.loadCityContentByCoordinates(User.getInstance().getCoordinates());
        presenter.requestCoordinates();
        presenter.loadCitiesList();

        return view;
    }

    public void initCitySpinner() {
        initCitySpinnerList();
        initCitySpinnerClickListener();
    }

    public void setCitySelectedName(String citySelected) {
        Timber.e("setCitySelectedName %s", citySelected);
        this.selectedCity = citySelected;
    }

    private void initCitySpinnerList() {
        setCityArrayAdapter();
        setCityStringNames(listCreator.citiesListToCitiesNameList(cityObjectList));
        if (this.cityStringNames == null)
            this.cityStringNames = Arrays.asList(getResources().getStringArray(R.array.cities));
        addNamesToCitySpinner(cityStringNames);
        addCityArrayAdapterToSpinner();
    }


    public void setCityArrayAdapter() {
        Timber.e("setCityArrayAdapter");
        cityArrayAdapter = new ArrayAdapter<>(App.getInstance(), R.layout.start_page_cities_spinner_item);
    }


    public void addCityArrayAdapterToSpinner() {
        Timber.e("addCityArrayAdapterToSpinner");
        userCitySpinner.setAdapter(cityArrayAdapter);
    }

    @Override
    public void setCityStringNames(ArrayList<String> cityStringNames) {
        Timber.e("setCityStringNames %s", Arrays.toString(cityStringNames.toArray()));
        this.cityStringNames = cityStringNames;
    }

    @Override
    public void onSpinnerItemClick(String selectedCity) {
        Timber.e("OnItemSelected CityNames =" + cityStringNames + " \n City = " + selectedCity + "\n CitiesList = " + cityObjectList);
        setCitySelectedName(selectedCity);
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
                setCitySelectedName(spinnerCityName);
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
        cityStringNames = listCreator.citiesListToCitiesNameList(cityObjectList);
        addNamesToCitySpinner(cityStringNames);
    }

    public void addNamesToCitySpinner(List<String> cityStringNames) {
        if (cityArrayAdapter != null) {
            for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
                String item = cityArrayAdapter.getItem(i);
                cityStringNames.remove(item);
            }
            cityArrayAdapter.addAll(cityStringNames);
        }
    }


    @Override
    public void setCityContentByCoordinates(CityContent cityContent) {
        this.cityContent = cityContent;
        hideCityArticlesFragment();
        //If no city is selected or loaded and if the info is related to the city selected
        presenter.setCity(cityContent);
        presenter.addToCityList(city, true);
        String cityName = listCreator.formatPlaceName(listCreator.cityToString(city));
        setCitySelectedName(cityName);
        if (articlesReceiver != null && this.cityContent != null && city != null) {
            presenter.setCityArcticles();
        }
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
        this.cityContent = cityContent;
        hideCityArticlesFragment();
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startPageLocationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestCoordinates() {
        startPageLocationManager.requestCoordinates(this.getActivity());
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.fragment_container), "We need GPS to show your city specific content", Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void setCity(CityContent cityContent) {
        if (cityContent != null) {
            int status = cityContent.getStatus();
            if (status == CODE_OK) {
                city = cityContent.getCity();
            } else if (status == CODE_ERROR) {
                city = cityContent.getContext();
            }
        }
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
        if (city != null && city.getArticleLinksContainer() != null) {
            articlesReceiver.setArticles(city.getArticleLinksContainer().getArticleLinkList());
            showCityArticlesFragment();
            decideCityFragmentContainerTitleVisibility();
        }
    }

    @Override
    public void onPermissionResult(boolean granted) {
        startPageLocationManager.onPermissionResult(granted);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestLocation() {
        startPageLocationManager.requestLocation();
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

    public interface ArticlesReceiver {
        void setArticles(List<ArticleLink> articleLinks);
        int getArticlesNumber();
    }
}
