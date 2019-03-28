package com.travl.guide.ui.fragment.place;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.PlacePresenter;
import com.travl.guide.mvp.view.PlaceView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaceFragment extends MvpAppCompatFragment implements PlaceView {

    private static final String ARGUMENT_ONE_KEY = "arg1";
    private static final String ARGUMENT_TWO_KEY = "arg2";

    @BindView(R.id.text_view_place_title)
    TextView placeTitleTextView;
    @BindView(R.id.text_view_place_subtitle)
    TextView placeSubtitleTextView;
    @BindView(R.id.image_view_place)
    ImageView placeImageView;
    @BindView(R.id.text_view_place_address)
    TextView placeAddressTextView;
    @BindView(R.id.text_view_author_name)
    TextView placeAuthorNameTextView;
    @BindView(R.id.text_view_place)
    TextView placeTextView;

    @InjectPresenter
    PlacePresenter presenter;

    @Inject
    IImageLoader imageLoader;

    public static PlaceFragment getInstance(String parameter1, int parameter2) {
        PlaceFragment placeFragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_ONE_KEY, parameter1);
        args.putInt(ARGUMENT_TWO_KEY, parameter2);
        placeFragment.setArguments(args);
        return placeFragment;
    }

    @ProvidePresenter
    public PlacePresenter providePresenter() {
        String arg1 = getArguments().getString(ARGUMENT_ONE_KEY);
        int arg2 = getArguments().getInt(ARGUMENT_TWO_KEY);
        return new PlacePresenter(AndroidSchedulers.mainThread(), arg1, arg2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void setTitleTextView(String title) {
        placeTitleTextView.setText(title);
    }

    @Override
    public void setSubtitleTextView(String subtitle) {
        placeSubtitleTextView.setText(subtitle);
    }

    @Override
    public void setImageView(String imageUrl) {
        imageLoader.loadInto(placeImageView, imageUrl);
    }

    @Override
    public void setPlaceAddressTextView(String placeAddress) {
        placeAddressTextView.setText(placeAddress);
    }

    @Override
    public void setPlaceAuthorNameTextView(String userName) {
        placeAuthorNameTextView.setText(userName);
    }

    @Override
    public void setTextView(String text) {
        placeTextView.setText(text);
    }

}
