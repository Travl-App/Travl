package com.travl.guide.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @ProvidePresenter
    public PlacePresenter providePresenter() {
        return new PlacePresenter(AndroidSchedulers.mainThread());
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
