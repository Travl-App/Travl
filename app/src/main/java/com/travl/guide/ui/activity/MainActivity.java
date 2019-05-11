package com.travl.guide.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.travl.guide.mvp.presenter.main.MainPresenter;
import com.travl.guide.mvp.view.main.MainView;
import com.travl.guide.navigator.CurrentScreen;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;
import com.travl.guide.ui.fragment.articles.travlzine.TravlZineArticlesFragment;
import com.travl.guide.ui.fragment.drawer.BottomNavigationDrawerBehavior;
import com.travl.guide.ui.fragment.drawer.BottomNavigationDrawerListener;
import com.travl.guide.ui.fragment.favorite.FavoriteFragment;
import com.travl.guide.ui.fragment.map.MapsFragment;
import com.travl.guide.ui.fragment.place.PlaceFragment;
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

public class MainActivity extends MvpAppCompatActivity implements MainView, BottomNavigationDrawerListener {

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

    private Fragment fragmentContainer;
    private CurrentScreen.Screen screen;
    private CoordinatesProvider coordinatesProvider;
    private SharedDataProvider sharedDataProvider;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

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
            Timber.e("Транзакция фрагментов. Команда = " + command.toString() + " фрагмент = " + nextFragment.getClass());
            if (nextFragment instanceof TravlZineArticlesFragment) {
                Timber.e("Смена фрагмента на %s", nextFragment.getClass());
                screen = CurrentScreen.INSTANCE.travlzine();
                presenter.onMoveToPlaceScreen();
            } else if (nextFragment instanceof MapsFragment) {
                Timber.e("Смена фрагмента на %s", nextFragment.getClass());
                screen = CurrentScreen.INSTANCE.map();
                presenter.onMoveToMapScreen();
            } else if (nextFragment instanceof StartPageFragment) {
                Timber.e("Смена фрагмента на %s", nextFragment.getClass());
                screen = CurrentScreen.INSTANCE.start();
                presenter.onMoveToStartPageScreen();
            } else if (nextFragment instanceof PlaceFragment) {
                Timber.e("Смена фрагмента на %s", nextFragment.getClass());
                screen = CurrentScreen.INSTANCE.post();
                presenter.onMoveToPostScreen();
            } else if (nextFragment instanceof FavoriteFragment) {
                Timber.e("Смена фрагмента на %s", nextFragment.getClass());
                screen = CurrentScreen.INSTANCE.favorite();
                presenter.onMoveToFavoriteScreen();
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
        Timber.e("OnBackPressed");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof FavoriteFragment) {
            Timber.e("to place screen");
            router.exit();
        } else if (fragment instanceof PlaceFragment) {
            Timber.e("to map screen");
            presenter.toMapScreen();
        } else if (!(fragment instanceof StartPageFragment)) {
            Timber.e("To start page");
            presenter.toStartPageScreen();
        } else {
            Timber.e("Finish");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        presenter.initUI();
        presenter.initEvents();

        fragmentContainer = getSupportFragmentManager().findFragmentById(R.id.container);
        if (savedInstanceState == null && fragmentContainer == null) {
            navigator.applyCommands(new Command[]{new Replace(new Screens.StartPageScreen())});
        }
    }

    @Override
    public void initUI() {
        Timber.d("initUI");
        navigationDrawer = new BottomNavigationDrawerBehavior();
        screen = CurrentScreen.INSTANCE.getCurrentScreen();
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
        fab.setOnClickListener(view -> {
            presenter.toMapScreen();
        });
    }

    public void onMoveToMapScreen() {
        Timber.d("onMoveToMapScreen");
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bar.getMenu().clear();
    }

    public void onMoveToFavoriteScreen() {
        Timber.d("onMoveToFavoriteScreen");
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setOnClickListener(view -> {
            presenter.toMapScreen();
        });
    }

    public void onMoveToStartPageScreen() {
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setOnClickListener(view -> {
            presenter.toMapScreen();
        });
    }

    @Override
    public void onMoveToPostScreen() {
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setOnClickListener(view -> {
            presenter.toMapScreen();
        });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof CoordinatesProvider) {
            this.coordinatesProvider = (CoordinatesProvider) fragment;
        }
        if (fragment instanceof SharedDataProvider) {
            this.sharedDataProvider = (SharedDataProvider) fragment;
        }
    }

    @Override
    public void toMapScreen() {
        double[] coordinates = null;
        if (coordinatesProvider != null)
            coordinates = coordinatesProvider.getCoordinates();
        router.navigateTo(new Screens.MapScreen(coordinates));
    }

    @Override
    public void toFavoriteScreens() {
        router.navigateTo(new Screens.FavoriteScreens());
    }

    @Override
    public void toTravlZineScreen() {
        router.navigateTo(new Screens.PlacesScreen());
    }

    @Override
    public void toStartPageScreen() {
        router.navigateTo(new Screens.StartPageScreen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_main_menu, menu);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (screen) {
            case StartPage:
                fab.setImageDrawable(getDrawable(R.drawable.ic_geo_map));
                menu.findItem(R.id.app_bar_search).setVisible(true);
                menu.findItem(R.id.app_bar_post_shared).setVisible(false);
                menu.findItem(R.id.app_bar_post_favorite).setVisible(false);
                invalidateOptionsMenu();
                break;
            case TravlzinePage:
                fab.setImageDrawable(getDrawable(R.drawable.ic_geo_map));
                menu.findItem(R.id.app_bar_search).setVisible(true);
                menu.findItem(R.id.app_bar_post_shared).setVisible(false);
                menu.findItem(R.id.app_bar_post_favorite).setVisible(false);
                invalidateOptionsMenu();
                break;
            case MapPage:
                fab.setImageDrawable(getDrawable(R.drawable.ic_search));
                menu.findItem(R.id.app_bar_search).setVisible(false);
                menu.findItem(R.id.app_bar_post_shared).setVisible(false);
                menu.findItem(R.id.app_bar_post_favorite).setVisible(false);
                invalidateOptionsMenu();
                break;
            case PostPage:
                fab.setImageDrawable(getDrawable(R.drawable.ic_show_on_map));
                menu.findItem(R.id.app_bar_search).setVisible(false);
                menu.findItem(R.id.app_bar_post_shared).setVisible(true);
                menu.findItem(R.id.app_bar_post_favorite).setVisible(true);
                invalidateOptionsMenu();
                break;
            case FavoritePage:
                fab.setImageDrawable(getDrawable(R.drawable.ic_show_on_map));
                menu.findItem(R.id.app_bar_search).setVisible(false);
                menu.findItem(R.id.app_bar_post_shared).setVisible(false);
                menu.findItem(R.id.app_bar_post_favorite).setVisible(false);
                invalidateOptionsMenu();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Toast.makeText(this, "Search posts", Toast.LENGTH_SHORT).show();
                break;
            case R.id.app_bar_post_shared:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String link = sharedDataProvider == null ? "Oops, something went wrong, try again..." : sharedDataProvider.getSharedData();
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(intent, "Share link"));
                break;
            case R.id.app_bar_post_favorite:
                Toast.makeText(this, "Show favorite posts", Toast.LENGTH_SHORT).show();
                presenter.toFavoriteScreen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //BottomNavigationDrawerListener
    @Override
    public void navToMapScreen() {
        presenter.toMapScreen();
    }

    @Override
    public void navToTravlZineScreen() {
        presenter.toTravlZineScreen();
    }

    @Override
    public void navToFavoriteScreen() {
        presenter.toFavoriteScreen();
    }

    @Override
    public void navToStartPageScreen() {
        presenter.toStartPageScreen();
    }
}