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
import com.travl.guide.mvp.model.image.GlideImageLoader;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.PlaceCardPresenter;
import com.travl.guide.mvp.presenter.StartPagePresenter;
import com.travl.guide.mvp.view.PlaceCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaceCardFragment extends MvpAppCompatFragment implements PlaceCardView {

    @BindView(R.id.image_view_author_avatar)
    ImageView authorAvatarImageView;
    @BindView(R.id.text_view_author_name)
    TextView authorNameTextView;

    @BindView(R.id.text_view_place_card_title)
    TextView placeCardTitleTextView;

    @BindView(R.id.text_view_place_card_first_text)
    TextView placeCardFirstTextView;
    @BindView(R.id.image_view_place_card_first_image)
    ImageView placeCardFirstImageView;

    @BindView(R.id.text_view_place_card_second_text)
    TextView placeCardSecondTextView;
    @BindView(R.id.image_view_place_card_second_image)
    ImageView placeCardSecondImageView;

    @InjectPresenter
    PlaceCardPresenter presenter;

    private IImageLoader imageLoader;


    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_card_fragment, container, false);
        ButterKnife.bind(this, view);

        imageLoader = new GlideImageLoader();

        return view;
    }


    @Override
    public void setAuthorNameTextView(String userName) {
        authorNameTextView.setText(userName);
    }

    @Override
    public void setAuthorAvatarImageView(String imageUrl) {
        imageLoader.loadInto(authorAvatarImageView, imageUrl);
    }

    @Override
    public void setTitleTextView(String title) {
        placeCardTitleTextView.setText(title);
    }

    @Override
    public void setFirstTextView(String text) {
        placeCardFirstTextView.setText(text);
    }

    @Override
    public void setFirstImageView(String imageUrl) {
        imageLoader.loadInto(placeCardFirstImageView, imageUrl);
    }

    @Override
    public void setSecondTextView(String text) {
        placeCardSecondTextView.setText(text);
    }

    @Override
    public void setSecondImageView(String imageUrl) {
        imageLoader.loadInto(placeCardSecondImageView, imageUrl);
    }
}
