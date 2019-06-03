package com.travl.guide.ui.fragment.articles;

import android.content.Context;
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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.ArticlePresenter;
import com.travl.guide.mvp.view.articles.ArticleView;
import com.travl.guide.navigator.CurrentScreen;
import com.travl.guide.ui.App;
import com.travl.guide.ui.activity.OnMoveToNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ArticleFragment extends MvpAppCompatFragment implements ArticleView {

    private static final String ARTICLE_ID_KEY = "article id key";

    @BindView(R.id.article_toolbar)
    Toolbar toolbar;
    @BindView(R.id.article_content_layout)
    LinearLayout linearLayout;
    @Inject
    IImageLoader iImageLoader;
    @InjectPresenter
    ArticlePresenter presenter;
    private OnMoveToNavigator moveToNavigator;

    public static ArticleFragment getInstance(int articleId) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID_KEY, articleId);
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (moveToNavigator != null) {
            moveToNavigator.onMoveTo(CurrentScreen.INSTANCE.article());
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
    public ArticlePresenter providePresenter() {
        return new ArticlePresenter(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        App.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        setupToolbar();
        Bundle bundle = getArguments();
        if (bundle != null) {
            int articleId = getArguments().getInt(ARTICLE_ID_KEY);
            presenter.loadArticle(articleId);
        }
        return view;
    }

    @Override
    public void setTitle(String title) {
        Timber.e("Set title to = " + title);
        LayoutInflater layoutInflater = getLayoutInflater();
        View textLayout = layoutInflater.inflate(R.layout.article_title_text_view, null);
        TextView titleTextView = textLayout.findViewById(R.id.article_title);
        titleTextView.setText(title);
        linearLayout.addView(titleTextView);
        linearLayout.invalidate();
    }

    @Override
    public void setSubTitle(String subtitle) {
        Timber.e("Set subtitle to = " + subtitle);
        LayoutInflater layoutInflater = getLayoutInflater();
        View textLayout = layoutInflater.inflate(R.layout.article_subtitle_text_view, null);
        TextView subtitleTextView = textLayout.findViewById(R.id.article_subtitle);
        subtitleTextView.setText(subtitle);
        linearLayout.addView(subtitleTextView);
        linearLayout.invalidate();
    }

    @Override
    public void setDescription(String description) {
        Timber.e("Set description to = " + description);
        LayoutInflater layoutInflater = getLayoutInflater();
        View textLayout = layoutInflater.inflate(R.layout.article_description_text_view, null);
        TextView descriptionTextView = textLayout.findViewById(R.id.article_description);
        descriptionTextView.setText(description);
        linearLayout.addView(descriptionTextView);
        linearLayout.invalidate();
    }

    @Override
    public void setImageCover(String coverUrl) {
        Timber.e("Set coverUrl to = " + coverUrl);
        View view = getView();
        if (view != null) {
            ImageView coverImageView = view.findViewById(R.id.article_cover);
            iImageLoader.loadInto(coverImageView, coverUrl);
        }
    }

    @Override
    public void setArticlePlaceCover(String placeImageUrl, int placeId) {
        Timber.e("Set placeImageUrl to = " + placeImageUrl);
        LayoutInflater layoutInflater = getLayoutInflater();
        View imageLayout = layoutInflater.inflate(R.layout.article_place_cover_image_view, null);
        ImageView placeCoverImageView = imageLayout.findViewById(R.id.article_place_cover);
        iImageLoader.loadInto(placeCoverImageView, placeImageUrl);
        placeCoverImageView.setOnClickListener(v -> presenter.showPlace(placeId));
        linearLayout.addView(placeCoverImageView);
        linearLayout.invalidate();
    }

    @Override
    public void setArticlePlaceDescription(String articleText) {
        Timber.e("Set articleText to = " + articleText);
        LayoutInflater layoutInflater = getLayoutInflater();
        View textLayout = layoutInflater.inflate(R.layout.article_place_description_text_view, null);
        TextView descriptionTextView = textLayout.findViewById(R.id.article_place_description);
        descriptionTextView.setText(articleText);
        linearLayout.addView(descriptionTextView);
        linearLayout.invalidate();
    }

    @Override
    public void setAuthorImage(String authorImageUrl) {

    }

    @Override
    public void setCategory(String category) {

    }

    @Override
    public void setDate(String date) {

    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void onBackPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
        else throw new RuntimeException("Activity is null");
    }

}
