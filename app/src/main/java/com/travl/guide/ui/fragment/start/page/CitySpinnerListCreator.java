package com.travl.guide.ui.fragment.start.page;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.travl.guide.R;
import com.travl.guide.mvp.model.api.city.content.CitiesList;
import com.travl.guide.mvp.model.api.city.content.City;
import com.travl.guide.mvp.model.api.city.content.CityContent;
import com.travl.guide.ui.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CitySpinnerListCreator {
    private static final String COMMA = ", ";


    public static ArrayList<String> citiesListToCitiesNameList(CitiesList citiesList) {
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
    public static String formatCityLink(CitiesList.CityLink cityLink) {
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


    public static City getCityName(CityContent cityContent, ArrayAdapter<String> cityArrayAdapter) {
        Resources resources = App.getInstance().getResources();
        int status = cityContent.getStatus();
        City city = null;
        String placeName = null;
        if (status == 200) {
            city = cityContent.getCity();
            if (city != null) {
                String area = city.getArea();
                String region = city.getRegion();
                String country = city.getCountry();
                if (!area.equals(region)) {
                    placeName = area + COMMA + region + COMMA + country;
                } else {
                    placeName = area + COMMA + country;
                }

            }
        } else if (status == 404) {
            city = cityContent.getContext();
            if (city != null) {
                String place = city.getPlaceName();
                String region = city.getRegion();
                String country = city.getCountry();
                if (!place.equals(region)) {
                    placeName = place + COMMA + region + COMMA + country;
                } else {
                    placeName = place + COMMA + country;
                }
            }
        }
        placeName = formatPlaceName(placeName);
        if (city != null && placeName != null) {
            boolean isPlaceAdded = false;
            for (int i = 0; i < cityArrayAdapter.getCount(); i++) {
                String name = (String) Objects.requireNonNull(cityArrayAdapter.getItem(i));
                if (name.equals(resources.getStringArray(R.array.cities)[0])) {
                    cityArrayAdapter.remove(name);
                }
                if (name.contains(placeName)) {
                    isPlaceAdded = true;
                }
            }
            if (!isPlaceAdded) {
                cityArrayAdapter.insert(placeName, 0);
            }
        }
        return city;
    }

    @Nullable
    private static String formatPlaceName(String placeName) {
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
