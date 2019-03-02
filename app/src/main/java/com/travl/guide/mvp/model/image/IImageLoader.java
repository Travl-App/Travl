package com.travl.guide.mvp.model.image;

import android.widget.ImageView;

public interface IImageLoader {
    void loadInto(ImageView imageView, String url);
}
