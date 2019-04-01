package com.travl.guide.ui.fragment.place;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.place.PlacePresenter;
import com.travl.guide.mvp.view.place.PlaceView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaceFragment extends MvpAppCompatFragment implements PlaceView {

    private static final String PLACE_ID_KEY = "place id key";

    @BindView(R.id.post_toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_place_title)
    TextView placeTitleTextView;
    @BindView(R.id.text_view_place_subtitle)
    TextView placeSubtitleTextView;
    @BindView(R.id.imageSlider)
    SliderLayout placeSliderLayout;
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

    public static PlaceFragment getInstance(int placeId) {
        PlaceFragment placeFragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt(PLACE_ID_KEY, placeId);
        placeFragment.setArguments(args);
        return placeFragment;
    }

    @ProvidePresenter
    public PlacePresenter providePresenter() {
        int placeId = getArguments().getInt(PLACE_ID_KEY);
        return new PlacePresenter(AndroidSchedulers.mainThread(), placeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupToolbar();
        setupSliderLayout();
        return view;
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            presenter.backPressure();
            Toast.makeText(getContext(), "Назад", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSliderLayout() {
        placeSliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        placeSliderLayout.setScrollTimeInSec(1);

        for (int i = 0; i <= 3; i++) {

            SliderView sliderView = new DefaultSliderView(getActivity());

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 2:
                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(getActivity(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            placeSliderLayout.addSliderView(sliderView);
        }
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
    public void setImageSlider(String imageUrl) {
//        imageLoader.loadInto(placeImageView, imageUrl);
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