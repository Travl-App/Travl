package com.travl.guide.ui.activity;

import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.MainPresenter;
import com.travl.guide.mvp.view.MainView;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.places.PlacesFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Screen;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter presenter;
    @Inject
    NavigatorHolder navigatorHolder;
    @BindView(R.id.app_bar_fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_app_bar)
    BottomAppBar bar;

    private Screen screens;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;

    private Navigator navigator = new SupportAppNavigator(this, R.id.container) {
        @Override
        public void applyCommands(Command[] commands) {
            super.applyCommands(commands);
        }

        @Override
        protected void setupFragmentTransaction(Command command, Fragment currentFragment, Fragment nextFragment, FragmentTransaction fragmentTransaction) {
            super.setupFragmentTransaction(command, currentFragment, nextFragment, fragmentTransaction);
            if(command instanceof Replace && nextFragment instanceof PlacesFragment) {
                toMapScreen();
            } else if(command instanceof Replace && nextFragment instanceof MapsFragment) {
                toPlacesScreen();
            }
        }
    };

    @ProvidePresenter
    public MainPresenter providePresenter() {
        MainPresenter presenter = new MainPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
        setSupportActionBar(bar);
        initEvents();
        initDefaultScreen(savedInstanceState);
    }

    public void initDefaultScreen(Bundle savedInstanceState) {
        Fragment fragmentContainer = getSupportFragmentManager().findFragmentById(R.id.container);
        if(savedInstanceState == null && fragmentContainer == null) {
            presenter.initPlacesScreen();
        }
        presenter.showCurrentFragment();
    }


    @Override
    public void showCurrentFragment() {
        navigator.applyCommands(new Command[] {new Replace(screens)});
    }

    @Override
    public void initPlacesScreen() {
        screens = new Screens.PlacesScreen();
    }

    @Override
    public void initMapScreen() {
        screens = new Screens.MapScreen();
    }

    @Override
    public void replaceScreen() {
        presenter.replaceScreen(screens);
    }

    private void initEvents() {
        fab.setOnClickListener(view -> presenter.changingScreen());
        bar.setNavigationOnClickListener(view -> bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag()));
    }

    private void toPlacesScreen() {
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
    }

    private void toMapScreen() {
        invalidateOptionsMenu();
        fab.setImageDrawable(getDrawable(R.drawable.ic_geo_map));
        fab.setOnClickListener(view -> {
            presenter.initMapScreen();
            presenter.changingScreen();
        });
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.app_bar_fav:
                Toast.makeText(this, "Show favorite posts", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}