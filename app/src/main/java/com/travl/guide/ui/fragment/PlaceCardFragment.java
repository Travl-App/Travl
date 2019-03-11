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


    @ProvidePresenter
    public StartPagePresenter providePresenter() {
        return new StartPagePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_card_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void setAuthorNameTextView(String userName) {

    }

    @Override
    public void setAuthorAvatarImageView(String imageUrl) {

    }

    @Override
    public void setTitleTextView(String title) {

    }

    @Override
    public void setFerstTextView(String text) {

    }

    @Override
    public void setFirstImageView(String imageUrl) {

    }

    @Override
    public void setSecondTextView(String text) {

    }

    @Override
    public void setSecondImageView(String imageUrl) {

    }
}
