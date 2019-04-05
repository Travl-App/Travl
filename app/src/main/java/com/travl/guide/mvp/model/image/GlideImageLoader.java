package com.travl.guide.mvp.model.image;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideImageLoader implements IImageLoader {
    @Override
    public void loadInto(ImageView imageView, String url) {
        Glide.with(imageView).load(url).into(imageView);
    }

    @Override
    public void loadInto(ImageView imageView, Drawable drawable) {
        Glide.with(imageView).load(drawable).into(imageView);
    }
}
