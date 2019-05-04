package com.travl.guide.mvp.model.location;

import java.util.List;

public interface LocationReceiver {

    void requestPermissions(String[] strings, int locationPermissionsRequestCode);

    void onExplanationNeeded(List<String> permissionsToExplain);

    void onLocationPermissionResult(boolean granted);
}
