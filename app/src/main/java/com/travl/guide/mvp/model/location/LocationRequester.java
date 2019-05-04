package com.travl.guide.mvp.model.location;

import android.support.annotation.NonNull;

public interface LocationRequester {
    void initLocationListener(LocationPresenter presenter);

    void requestLocation();

    void onPermissionResult(LocationPresenter presenter, boolean granted);

    void onRequestPermissionsResult(LocationPresenter presenter, int requestCode,
                                    @NonNull String[] permissions, @NonNull int[] grantResults);

    void requestCoordinates(LocationReceiver locationReceiver);
}
