package com.travl.guide.ui.fragment.start.page;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.travl.guide.R;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.presenter.start.page.StartPagePresenter;
import com.travl.guide.ui.App;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CitySpinnerListCreator {
    private static final String COMMA = ", ";

    public ArrayList<String> citiesListToCitiesNameList(CitiesList citiesList) {
        List<CitiesList.CityLink> cityLinks;
        ArrayList<String> cityNames = new ArrayList<>();
        String title = "";
        if (citiesList != null && (cityLinks = citiesList.getCities()) != null) {
            for (int i = 0; i < citiesList.getCities().size(); i++) {
                CitiesList.CityLink cityLink = cityLinks.get(i);
                title = formatCityLink(cityLink);
                if (title != null) cityNames.add(title);
            }
        }
        return cityNames;
    }

    @Nullable
    public String formatCityLink(CitiesList.CityLink cityLink) {
        String title = null;
        String area = cityLink.getArea();
        String region = cityLink.getRegion();
        String country = cityLink.getCountry();
        if (!area.equals(region)) {
            title = area + COMMA + region + COMMA + country;
        } else {
            title = area + COMMA + country;
        }
        title = formatPlaceName(title);
        return title;
    }

    @Nullable
    public void addToCityList(City city, ArrayAdapter<String> cityArrayAdapter, StartPagePresenter startPagePresenter, boolean isUserCity) {
        startPagePresenter.addNamesToCitySpinner();
        Resources resources = App.getInstance().getResources();
        String placeName = cityToString(city);
        placeName = formatPlaceName(placeName);
        String userLocationMarker = resources.getString(R.string.user_location_marker);
        //Check for duplicates and blank
        if (cityArrayAdapter != null && city != null && placeName != null) {
            boolean isPlaceAdded = false;
            for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
                String name = cityArrayAdapter.getItem(i);
                Timber.e("Name = " + name + "PlaceName =" + placeName);
                if (name != null) {
                    //Remove blank "Choose city:"
                    if (name.equals(resources.getStringArray(R.array.cities)[0])) {
                        startPagePresenter.removeFromCitySpinnerAdapter(name);
                    }
                    if (name.startsWith(userLocationMarker)) {
                        startPagePresenter.removeFromCitySpinnerAdapter(name);
                    }
                    if (name.contains(placeName) || name.contains(userLocationMarker + " " + placeName)) {
                        Timber.e("Place =" + name + " is added");
                        isPlaceAdded = true;
                    }
                }
            }
            //Remove duplicates
            if (isPlaceAdded) {
                startPagePresenter.removeFromCitySpinnerAdapter(placeName);
            }
            //Add "You are here:" if it is User's city
            if (isUserCity && placeName != null) {
                placeName = resources.getString(R.string.user_location_marker) + " " + placeName;
            }
            startPagePresenter.placeSelectedCityOnTop(placeName);
        }
    }

    public String cityToString(City city) {
        String placeName = null;
        if (city != null) {
            String area = city.getArea();
            String place = city.getPlaceName();
            String region = city.getRegion();
            String country = city.getCountry();
            if (area != null) {
                if (!area.equals(region)) {
                    placeName = area + COMMA + region + COMMA + country;
                } else {
                    placeName = area + COMMA + country;
                }
            } else if (place != null) {
                if (!place.equals(region)) {
                    placeName = place + COMMA + region + COMMA + country;
                } else {
                    placeName = place + COMMA + country;
                }
            }
        }
        return placeName;
    }

    @Nullable
    public String formatPlaceName(String placeName) {
        if (placeName == null) return null;
        Resources resources = App.getInstance().getResources();
        String notFound = resources.getString(R.string.city_not_found);
        String result = placeName.replaceAll(notFound + COMMA, "");
        result = result.replaceAll(notFound, "");
        boolean onlyCommasLeft = true;
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) != ',' && result.charAt(i) != ' ') {
                onlyCommasLeft = false;
            }
        }
        if (onlyCommasLeft || result.isEmpty()) return null;
        return result;
    }
}
