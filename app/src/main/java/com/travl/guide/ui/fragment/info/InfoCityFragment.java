package com.travl.guide.ui.fragment.info;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.info.InfoCityPresenter;
import com.travl.guide.mvp.view.info.InfoCityView;
import com.travl.guide.ui.App;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InfoCityFragment extends MvpAppCompatFragment implements InfoCityView {

	public static final String KEY_CITY_ID = "key city id";

	public static InfoCityFragment newInstance(int cityId){
		InfoCityFragment fragment = new InfoCityFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_CITY_ID, cityId);
		fragment.setArguments(args);
		return fragment;
	}

	private Unbinder unbinder;
	@BindView(R.id.city_toolbar) Toolbar toolbar;
	@BindView(R.id.city_image) AppCompatImageView imageView;
	@BindView(R.id.city_name) TextView nameView;
	@BindView(R.id.city_article) TextView articleView;
	@BindView(R.id.city_wikipedia) AppCompatButton button;
	@BindDrawable(R.drawable.ic_back) Drawable back;

	@Inject	IImageLoader imageLoader;;

	@InjectPresenter InfoCityPresenter cityPresenter;

	@ProvidePresenter
	InfoCityPresenter providePresenter(){
		Bundle args = getArguments();
		int cityId = 0;
		if (args != null){
			cityId = args.getInt(KEY_CITY_ID);
		}
		InfoCityPresenter cityPresenter = new InfoCityPresenter(cityId);
		App.getInstance().getAppComponent().inject(cityPresenter);
		cityPresenter.loadCity();
		return cityPresenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.city_info_fragment, container, false);
		unbinder = ButterKnife.bind(this, view);
		App.getInstance().getAppComponent().inject(this);
		setupToolbar();
		return view;
	}

	private void setupToolbar() {
		toolbar.setNavigationIcon(back);
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
	}

	public void onBackPressed() {
		if (getActivity() != null) getActivity().onBackPressed();
	}

	@Override
	public void showTitle(String title) {
		nameView.setText(title);
	}

	@Override
	public void showImageCity(String url) {
		imageLoader.loadInto(imageView, url);
	}

	@Override
	public void showDescriptionCity(String description) {
		articleView.setText(description);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
