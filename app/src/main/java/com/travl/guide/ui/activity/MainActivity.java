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
import com.travl.guide.ui.fragment.start.page.StartPageFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;
import timber.log.Timber;

public class MainActivity extends MvpAppCompatActivity implements MainView, StartPageFragment.StartPageEventsListener, BottomNavigationDrawerListener {

    @Inject
    Router router;
    @InjectPresenter
    MainPresenter presenter;
    @Inject
    NavigatorHolder navigatorHolder;
    @BindView(R.id.app_bar_fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_app_bar)
    BottomAppBar bar;

    private BottomNavigationDrawerBehavior navigationDrawer;

    private Navigator navigator = new SupportAppNavigator(this, R.id.container) {
        @Override
        public void applyCommands(Command[] commands) {
            super.applyCommands(commands);
        }

        @Override
        protected void setupFragmentTransaction(Command command, Fragment currentFragment, Fragment nextFragment, FragmentTransaction fragmentTransaction) {
            super.setupFragmentTransaction(command, currentFragment, nextFragment, fragmentTransaction);
            fragmentTransaction.addToBackStack(null);
            if(command instanceof Replace && nextFragment instanceof PlacesFragment) {
                Timber.d("Смена фрагмента на %s", nextFragment.getClass());
                presenter.onMoveToPlaceScreen();
            } else if(command instanceof Replace && nextFragment instanceof MapsFragment) {
                Timber.d("Смена фрагмента на %s", nextFragment.getClass());
                presenter.onMoveToMapScreen();
            }else if (command instanceof Replace && nextFragment instanceof StartPageFragment){
                presenter.onMoveToStartPageScreen();
            }
        }
    };

    @ProvidePresenter
    public MainPresenter providePresenter() {
        MainPresenter presenter = new MainPresenter();
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof StartPageFragment)) {
            presenter.toStartPageScreen();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.initUI();
        presenter.initEvents();

        Fragment fragmentContainer = getSupportFragmentManager().findFragmentById(R.id.container);
        if(savedInstanceState == null && fragmentContainer == null) {
            navigator.applyCommands(new Command[] {new Replace(new Screens.StartPageScreen())});
        }
    }

    @Override
    public void initUI() {
        Timber.d("initUI");
        navigationDrawer = new BottomNavigationDrawerBehavior();
        setSupportActionBar(bar);
    }

    @Override
    public void initEvents() {
        Timber.d("initEvents");
        bar.setNavigationOnClickListener(view -> {
            if (!getSupportFragmentManager().executePendingTransactions() && !navigationDrawer.isAdded()) {
                navigationDrawer.show(getSupportFragmentManager(),
                        navigationDrawer.getTag());
            }
        });

    }

    public void onMoveToPlaceScreen() {
        Timber.d("onMoveToPlaceScreen");
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setImageDrawable(getDrawable(R.drawable.ic_geo_map));
        fab.setOnClickListener(view -> {
            presenter.toMapScreen();
        });
    }


    public void onMoveToMapScreen() {
        Timber.d("onMoveToMapScreen");
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        fab.setImageDrawable(getDrawable(R.drawable.ic_search));
    }


    public void onMoveToStartPageScreen() {
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setImageDrawable(getDrawable(R.drawable.ic_geo_map));
        fab.setOnClickListener(view -> {
            presenter.toMapScreen();
        });
    }

    @Override
    public void toMapScreen() {
        router.replaceScreen(new Screens.MapScreen());
    }

    @Override
    public void toPlaceScreen() {
        router.replaceScreen(new Screens.PlacesScreen());
    }

    @Override
    public void toStartPageScreen() {
        router.replaceScreen(new Screens.StartPageScreen());
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


    //StartPageEventsListener
    @Override
    public void onPlaceCollectionsClick() {
        presenter.toPlaceScreen();
    }

    @Override
    public void onMapClick() {
        presenter.toMapScreen();
    }

    //BottomNavigationDrawerListener
    @Override
    public void navToMapScreen() {
        presenter.toMapScreen();
    }

    @Override
    public void navToPlaceScreen() {
        presenter.toPlaceScreen();
    }

    @Override
    public void navToStartPageScreen() {
        presenter.toStartPageScreen();
    }
}