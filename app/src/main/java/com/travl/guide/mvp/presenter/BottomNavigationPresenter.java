package com.travl.guide.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.view.BottomNavigationView;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.Screen;

@InjectViewState
public class BottomNavigationPresenter extends MvpPresenter<BottomNavigationView> {
    @Inject
    Router router;
    Scheduler scheduler;

    public BottomNavigationPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void replaceScreen(Screen screen) {
        router.replaceScreen(screen);
    }

    public void changeScreen() {
        getViewState().replaceScreen();
    }
}
