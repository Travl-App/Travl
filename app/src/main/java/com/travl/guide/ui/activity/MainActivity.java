package com.travl.guide.ui.activity;

import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.travl.guide.R;
import com.travl.guide.navigator.Screens;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;

public class MainActivity extends AppCompatActivity {

    @Inject
    Router router;
    @Inject
    NavigatorHolder navigatorHolder;
    @BindView(R.id.app_bar_fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_app_bar)
    BottomAppBar bar;

    private BottomNavigationDrawerFragment fragment;

    private Navigator navigator = new SupportAppNavigator(this, R.id.container) {
        @Override
        public void applyCommands(Command[] commands) {
            super.applyCommands(commands);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragment = new BottomNavigationDrawerFragment();
        setSupportActionBar(bar);
        initEvents();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(savedInstanceState == null && fragment == null) {
            navigator.applyCommands(new Command[] {new Replace(new Screens.PlacesScreen())});
        }
    }

    private void initEvents() {
        bar.setNavigationOnClickListener(view -> fragment.show(getSupportFragmentManager(), fragment.getTag()));
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
                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.app_bar_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}