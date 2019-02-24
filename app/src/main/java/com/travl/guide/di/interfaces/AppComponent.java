package com.travl.guide.di.interfaces;

import com.travl.guide.di.modules.AppModule;
import com.travl.guide.di.modules.CiceroneModule;
import com.travl.guide.mvp.presenter.CollectionsPresenter;
import com.travl.guide.ui.activity.MainActivity;
import com.travl.guide.ui.fragment.Collections.CollectionsFragment;

import javax.inject.Singleton;

import dagger.Component;

//Created by Pereved on 23.02.2019.
@Singleton
@Component(modules = {
        AppModule.class,
        CiceroneModule.class
})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(CollectionsPresenter presenter);

    void inject(CollectionsFragment mainFragment);
}