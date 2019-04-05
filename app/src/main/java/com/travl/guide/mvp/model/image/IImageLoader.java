package com.travl.guide.mvp.model.image;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface IImageLoader {
    void loadInto(ImageView imageView, String url);

    void loadInto(ImageView imageView, Drawable drawable);
}
