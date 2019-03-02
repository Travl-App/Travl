package com.travl.guide.di.modules;

import com.travl.guide.mvp.model.image.GlideImageLoader;
import com.travl.guide.mvp.model.image.IImageLoader;

import dagger.Module;
import dagger.Provides;

@Module
public class ImageLoaderModule {
    @Provides
    public IImageLoader getImageLoader() {
        return new GlideImageLoader();
    }
}
