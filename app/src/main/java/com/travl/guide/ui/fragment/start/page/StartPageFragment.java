package com.travl.guide.ui.fragment.start.page;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.StartPagePresenter;
import com.travl.guide.mvp.view.StartPageView;
import com.travl.guide.ui.fragment.places.PlacesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class StartPageFragment extends MvpAppCompatFragment implements StartPageView {

    @BindView(R.id.map_image_view)
    ImageView mapImageView;
    @BindView(R.id.places_image_view)
    ImageView placesImageView;
    @InjectPresenter
    StartPagePresenter presenter;
    private StartPageEventsListener startPageEventsListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        startPageEventsListener = (StartPageEventsListener) context;
    }

    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        StartPagePresenter presenter = new StartPagePresenter(AndroidSchedulers.mainThread());
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_page_fragment, container, false);
        ButterKnife.bind(this, view);
        presenter.initPlacesFragment();
        return view;
    }

    @OnClick(R.id.places_image_view)
    public void onPlaceCollectionsClick() {
        if (startPageEventsListener != null) {
            startPageEventsListener.onPlaceCollectionsClick();
        }
    }

    @OnClick(R.id.map_image_view)
    public void onMapClick() {
        if (startPageEventsListener != null) {
            startPageEventsListener.onMapClick();
        }
    }

    @Override
    public void initPlacesFragment() {
        Fragment placesFragment = new PlacesFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.start_page_places_container, placesFragment).commit();
    }

    public interface StartPageEventsListener {
        void onPlaceCollectionsClick();

        void onMapClick();
    }
}
