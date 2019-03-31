package com.travl.guide.ui.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.travl.guide.ui.App;

public class NetworkStatus {
    private static boolean isAirplane() {
        return Settings.Global.getInt(App.getInstance().getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static Status getStatus() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return Status.WIFI;

                case ConnectivityManager.TYPE_ETHERNET:
                    return Status.ETHERNET;

                case ConnectivityManager.TYPE_MOBILE:
                    return Status.MOBILE;
            }
            return Status.OTHER;
        }
        return Status.OFFLINE;
    }

    public static boolean isOnline() {
        return !getStatus().equals(Status.OFFLINE);
    }

    public static boolean isWifi() {
        return getStatus().equals(Status.WIFI);
    }

    public static boolean isEthernet() {
        return getStatus().equals(Status.ETHERNET);
    }

    public static boolean isMobile() {
        return getStatus().equals(Status.MOBILE);
    }

    public static boolean isOffline() {
        return getStatus().equals(Status.OFFLINE);
    }

    public enum Status {
        WIFI,
        MOBILE,
        ETHERNET,
        OTHER,
        OFFLINE,
    }
}
