package com.travl.guide.ui.fragment.place;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.travl.guide.mvp.presenter.place.PlacePresenter;
import com.travl.guide.mvp.view.place.PlaceView;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.CoordinatesProvider;
import com.travl.guide.ui.activity.SharedDataProvider;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaceFragment extends MvpAppCompatFragment implements PlaceView, CoordinatesProvider, SharedDataProvider {

    private static final String PLACE_ID_KEY = "place id key";

    @BindView(R.id.post_toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_place_category)
    TextView placeCategoryTextView;
    @BindView(R.id.text_view_place_title)
    TextView placeTitleTextView;
    @BindView(R.id.text_view_place_subtitle)
    TextView placeSubtitleTextView;
    @BindView(R.id.imageSlider_place)
    SliderLayout placeSliderLayout;
    @BindView(R.id.text_view_place_address)
    TextView placeAddressTextView;
    @BindView(R.id.text_view_place_route)
    TextView placeRouteTextView;
    @BindView(R.id.image_view_place_popularity)
    ImageView placePopularityImageView;
    @BindView(R.id.text_view_place_description)
    TextView placeDescriptionTextView;
    @BindView(R.id.button_place)
    Button placeButton;
    @BindView(R.id.text_view_author_name)
    TextView placeAuthorNameTextView;

    @InjectPresenter
    PlacePresenter presenter;

    @Inject
    @Named("baseUrl")
    String baseUrl;

    private double[] placeCoordinates;

    public static PlaceFragment getInstance(int placeId) {
        PlaceFragment placeFragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt(PLACE_ID_KEY, placeId);
        placeFragment.setArguments(args);
        return placeFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDispose();
    }

    @ProvidePresenter
    public PlacePresenter providePresenter() {
        Bundle args = getArguments();
        int placeId = 0;
        if (args != null) {
            placeId = args.getInt(PLACE_ID_KEY);
        }
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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupSliderLayout() {
        placeSliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        placeSliderLayout.setAutoScrolling(false);
    }

    @Override
    public void setPlaceCategory(String placeCategory) {
        placeCategoryTextView.setText(placeCategory);
    }

    @Override
    public void setPlaceTitle(String placeTitle) {
        placeTitleTextView.setText(placeTitle);
    }

    @Override
    public void setPlaceSubtitle(String placeSubtitle) {
        placeSubtitleTextView.setText(placeSubtitle);
    }

    @Override
    public void setPlaceImages(List<String> placeImageUrls) {
        for (String imageUrl : placeImageUrls) {
            SliderView sliderView = new DefaultSliderView(getActivity());
            sliderView.setImageUrl(baseUrl + imageUrl);
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
//            sliderView.setDescription("setDescription");
            sliderView.setOnSliderClickListener(sliderView1 -> Toast.makeText(getActivity(), "This is slider", Toast.LENGTH_SHORT).show());
            placeSliderLayout.addSliderView(sliderView);
        }
    }

    @Override
    public void setPlaceAddress(String placeAddress) {
        placeAddressTextView.setText(placeAddress);
    }

    @Override
    public void setPlaceRoute(String placeRoute) {
        placeRouteTextView.setText(placeRoute);
    }

    @Override
    public void setPlacePopularity(int placePopularity) {
        placePopularityImageView.setBackgroundColor(placePopularity);
    }

    @Override
    public void setPlaceDescription(String placeDescription) {
        placeDescriptionTextView.setText(placeDescription);
    }

    @Override
    public void setPlaceAuthorName(String placeAuthorName) {
        placeAuthorNameTextView.setText(placeAuthorName);
    }

    public void onBackPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
        else throw new RuntimeException("Activity is null");
    }

    @Nullable
    @Override
    public double[] getCoordinates() {
        return placeCoordinates;
    }

    @Override
    public void setPlaceCoordinates(double[] placeCoordinates) {
        this.placeCoordinates = placeCoordinates;
    }

    @Override
    public String getSharedData() {
        return presenter.getPlaceUrl();
    }
}