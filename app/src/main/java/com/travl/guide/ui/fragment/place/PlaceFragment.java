package com.travl.guide.ui.fragment.place;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.travl.guide.navigator.CurrentScreen;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.CoordinatesProvider;
import com.travl.guide.ui.activity.OnMoveToNavigator;
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
    @BindView(R.id.layout_place_root)
    LinearLayout placeRootLayout;
    @BindView(R.id.text_view_place_category)
    TextView placeCategoryTextView;
    @BindView(R.id.text_view_place_title)
    TextView placeTitleTextView;
    //    @BindView(R.id.text_view_place_subtitle)
//    TextView placeSubtitleTextView;
    @BindView(R.id.imageSlider_place)
    SliderLayout placeSliderLayout;
    @BindView(R.id.layout_place_address)
    LinearLayout placeAddressLayout;
    @BindView(R.id.text_view_place_address)
    TextView placeAddressTextView;
    @BindView(R.id.layout_place_route)
    LinearLayout placeRouteLayout;
    @BindView(R.id.text_view_place_route)
    TextView placeRouteTextView;
    @BindView(R.id.image_view_place_popularity_1)
    ImageView placePopularityImageView1;
    @BindView(R.id.image_view_place_popularity_2)
    ImageView placePopularityImageView2;
    @BindView(R.id.image_view_place_popularity_3)
    ImageView placePopularityImageView3;
    @BindView(R.id.image_view_place_popularity_4)
    ImageView placePopularityImageView4;
    @BindView(R.id.image_view_place_popularity_5)
    ImageView placePopularityImageView5;
    ImageView[] placePopularityImageViews;
    @BindView(R.id.text_view_place_description)
    TextView placeDescriptionTextView;
    //    @BindView(R.id.button_place)
//    Button placeButton;
    @BindView(R.id.layout_author_name)
    LinearLayout placeAuthorNameLayout;
    @BindView(R.id.text_view_author_name)
    TextView placeAuthorNameTextView;

    @InjectPresenter
    PlacePresenter presenter;

    @Inject
    @Named("baseUrl")
    String baseUrl;

    private double[] placeCoordinates;
    private OnMoveToNavigator moveToNavigator;

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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (moveToNavigator != null) {
            moveToNavigator.onMoveTo(CurrentScreen.INSTANCE.place());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoveToNavigator) {
            moveToNavigator = (OnMoveToNavigator) context;

        }
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
        placePopularityImageViews = new ImageView[]{placePopularityImageView1, placePopularityImageView2, placePopularityImageView3, placePopularityImageView4, placePopularityImageView5};
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
        if (placeCategory == null) {
            placeRootLayout.removeView(placeCategoryTextView);
        } else {
            placeCategory = placeCategory.toUpperCase();
            Resources resources = App.getInstance().getResources();
            int color = 0;
            switch (placeCategory) {
                case "АРХИТЕКТУРА":
                    color = Color.parseColor(resources.getString(R.string.category_color_architecture));
                    break;
                case "ПОКУПКИ":
                    color = Color.parseColor(resources.getString(R.string.category_color_goods));
                    break;
                case "ЕДА":
                    color = Color.parseColor(resources.getString(R.string.category_color_food));
                    break;
                case "ЖИЛЬЕ":
                    color = Color.parseColor(resources.getString(R.string.category_color_habitation));
                    break;
                case "ОТЕЛЬ":
                    color = Color.parseColor(resources.getString(R.string.category_color_habitation));
                    break;
                case "ПРИРОДА":
                    color = Color.parseColor(resources.getString(R.string.category_color_nature));
                    break;
                case "STREETART":
                    color = Color.parseColor(resources.getString(R.string.category_color_streetart));
                    break;
                case "НАХОДКА":
                    color = Color.parseColor(resources.getString(R.string.category_color_trove));
                    break;
                case "URBEX":
                    color = Color.parseColor(resources.getString(R.string.category_color_urbex));
                    break;
                case "ВИД":
                    color = Color.parseColor(resources.getString(R.string.category_color_view));
                    break;
                default:
                    color = Color.parseColor(resources.getString(R.string.category_color_default));
                    break;
            }
            Drawable background = placeCategoryTextView.getBackground();
            if (background instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) background;
                gradientDrawable.setStroke(2, color);
            }
            placeCategoryTextView.setTextColor(color);
            placeCategoryTextView.setText(placeCategory);
        }
    }

    @Override
    public void setPlaceTitle(String placeTitle) {
        placeTitleTextView.setText(placeTitle);
    }

//    @Override
//    public void setPlaceSubtitle(String placeSubtitle) {
//        placeSubtitleTextView.setText(placeSubtitle);
//    }

    @Override
    public void setPlaceImages(List<String> placeImageUrls) {
        if (placeImageUrls != null) {
            for (String imageUrl : placeImageUrls) {
                SliderView sliderView = new DefaultSliderView(getActivity());
                sliderView.setImageUrl(baseUrl + imageUrl);
                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
//                sliderView.setDescription("setDescription");
                sliderView.setOnSliderClickListener(sliderView1 -> Toast.makeText(getActivity(), "This is slider", Toast.LENGTH_SHORT).show());
                placeSliderLayout.addSliderView(sliderView);
            }
        }
    }

    @Override
    public void setPlaceAddress(String placeAddress) {
        if (placeAddress == null) {
            placeRootLayout.removeView(placeAddressLayout);
        } else {
            placeAddressTextView.setText(placeAddress);
        }
    }

    @Override
    public void setPlaceRoute(String placeRoute) {
        if (placeRoute == null) {
            placeRootLayout.removeView(placeRouteLayout);
        } else {
            placeRouteTextView.setText(placeRoute);
        }
    }

    @Override
    public void setPlacePopularity(int placePopularity) {
        for (int i = 0; i < placePopularity; i++) {
            placePopularityImageViews[i].setBackground(getResources().getDrawable(R.drawable.circle_popularity_full));
        }
    }

    @Override
    public void setPlaceDescription(String placeDescription) {
        if (placeDescription == null) {
            placeRootLayout.removeView(placeDescriptionTextView);
        } else {
            placeDescriptionTextView.setText(placeDescription);
        }
    }

    @Override
    public void setPlaceAuthorName(String placeAuthorName) {
        if (placeAuthorName == null) {
            placeRootLayout.removeView(placeAuthorNameLayout);
        } else {
            placeAuthorNameTextView.setText(placeAuthorName);
        }
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