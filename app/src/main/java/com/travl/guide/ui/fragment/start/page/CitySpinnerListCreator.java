package com.travl.guide.ui.fragment.start.page;

import android.content.res.Resources;

import androidx.annotation.Nullable;

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
    private StartPagePresenter startPagePresenter;

    public CitySpinnerListCreator(StartPagePresenter startPagePresenter) {
        this.startPagePresenter = startPagePresenter;
    }

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
        String place = cityLink.getPlace();
        String region = cityLink.getRegion();
        String country = cityLink.getCountry();
        Timber.e("format area = " + area + " place = " + place + " region " + region + " country " + country);
        area = formatPlaceName(area);
        place = formatPlaceName(place);
        region = formatPlaceName(region);
        country = formatPlaceName(country);
        if (area != null && !area.isEmpty()) {
            title = area;
        } else if (place != null && !place.isEmpty()) {
            title = place;
        } else if (region != null && !region.isEmpty()) {
            title = region;
        } else if (country != null && !country.isEmpty()) {
            title = country;
        }
        title = formatPlaceName(title);
        Timber.e("title = " + title);
        return title;
    }

    @Nullable
    public void addToCityList(City city, boolean isUserCity,List<String> cityStringNames) {
        startPagePresenter.addNamesToCitySpinner();
        Resources resources = App.getInstance().getResources();
        String placeName = cityToString(city);
        placeName = formatPlaceName(placeName);
        String userLocationMarker = resources.getString(R.string.user_location_marker);
        //Check for duplicates and blank
        if (cityStringNames != null && city != null && placeName != null) {
            boolean isPlaceAdded = false;
            for (int i = 0; i < cityStringNames.size(); i++) {
                String name = cityStringNames.get(i);

                if (name != null) {
                    //Remove blank "Choose city:"
                    if (name.equals(resources.getStringArray(R.array.cities)[0])) {
                        startPagePresenter.removeCityFromList(name);
                    }
                    if (name.startsWith(userLocationMarker)) {
                        startPagePresenter.removeCityFromList(name);
                    }
                    if (name.contains(placeName) || name.contains(userLocationMarker + " " + placeName)) {

                        isPlaceAdded = true;
                    }
                }
            }
            //Remove duplicates
            if (isPlaceAdded) {
                startPagePresenter.removeCityFromList(placeName);
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
            Timber.e("area = " + area + " place = " + place + " region " + region + " country " + country);
            area = formatPlaceName(area);
            place = formatPlaceName(place);
            region = formatPlaceName(region);
            country = formatPlaceName(country);
            if (area != null && !area.isEmpty()) {
                placeName = area;
            } else if (place != null && !place.isEmpty()) {
                placeName = place;
            } else if (region != null && !region.isEmpty()) {
                placeName = region;
            } else if (country != null && !country.isEmpty()) {
                placeName = country;
            }
            Timber.e("placeName = " + placeName);
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
