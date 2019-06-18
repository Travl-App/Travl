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
import com.travl.guide.mvp.presenter.info.InfoCityPresenter;
import com.travl.guide.mvp.view.info.InfoCityView;
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

	@InjectPresenter InfoCityPresenter cityPresenter;

	@ProvidePresenter
	InfoCityPresenter providePresenter(){
		Bundle args = getArguments();
		int cityId = 0;
		if (args != null){
			cityId = args.getInt("key city id");
		}
		InfoCityPresenter cityPresenter = new InfoCityPresenter(cityId);
		return cityPresenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.city_info_fragment, container, false);
		unbinder = ButterKnife.bind(this, view);
		setupToolbar();
		init();
		return view;
	}

	/* временный метод только для макета потом удалить все

	 */
	private void init(){
		imageView.setImageResource(R.drawable.ic_launcher_background);
		nameView.setText("Санкт-Петербург");
		articleView.setText("Санкт-Петербу́рг (с 18 [31] августа 1914 года до 26 января 1924[7] года — Петрогра́д, с 26 января 1924 года до 6 сентября 1991 года[3] — Ленингра́д) — второй по численности населения город России[8]. Город федерального значения. Административный центр Северо-Западного федерального округа и Ленинградской области. Основан 16 (27) мая 1703 года Петром I. В 1712—1918 годах являлся столицей Российского государства[9][10].\n" +
			"Назван в честь Святого Петра, небесного покровителя царя-основателя, но со временем стал всё больше ассоциироваться с именем самого Петра I. Город исторически и культурно связан с рождением Российской империи и вхождением России в современную историю в роли европейской великой державы[11].\n" +
			"Расположен на северо-западе страны, на побережье Финского залива и в устье реки Невы. В Санкт-Петербурге находятся Конституционный суд Российской Федерации, Геральдический совет при Президенте Российской Федерации, органы власти Ленинградской области[12], Межпарламентская ассамблея СНГ. Также размещены главное командование Военно-морского флота и штаб Западного военного округа Вооружённых сил России[13].\n" +
			"Был центром трёх революций: 1905—1907 годов, Февральской и Октябрьской революций 1917 года[14]. Во время Великой Отечественной войны 1941—1945 годов 872 дня находился в блокаде, в результате которой более миллиона человек погибли. 1 мая 1945 года Ленинград был объявлен городом-героем. По состоянию на 2018 год в составе города федерального значения Санкт-Петербурга также находятся три города воинской славы: Кронштадт, Колпино, Ломоносов.\n" +
			"Население: 5 383 968[4] (2019) чел. Санкт-Петербург — самый северный в мире город с населением более одного миллиона человек. Также Санкт-Петербург является самым западным городом-миллионером России. Среди городов, полностью расположенных в Европе, он является третьим по населению, а также первым по численности жителей городом, не являющимся столицей[15]. Инновационный сценарий «Стратегии развития Санкт-Петербурга до 2030 года» предполагает, что к 2030 году население составит 5,9 миллиона человек[16]. Город — центр Санкт-Петербургской городской агломерации. Площадь: 1439[17] км², после расширения Москвы 1 июля 2012 года Санкт-Петербург является вторым по площади городом страны.\n" +
			"Санкт-Петербург — важный экономический, научный и культурный центр России, крупный транспортный узел. Исторический центр города и связанные с ним комплексы памятников входят в список объектов всемирного наследия ЮНЕСКО[18]; это один из самых важных в стране центров туризма. Среди наиболее значимых культурно-туристических объектов: Эрмитаж, Кунсткамера, Мариинский театр, Российская национальная библиотека, Русский музей, Петропавловская крепость, Исаакиевский собор[19], Невский проспект. На сохранение объектов культурного наследия направлена, в том числе, программа сохранения и развития исторического центра Санкт-Петербурга. В 2018 году Санкт-Петербург посетили около 8,5 миллионов туристов[20].");
	}


	private void setupToolbar() {
		toolbar.setNavigationIcon(back);
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
	}

	public void onBackPressed() {
		if (getActivity() != null) getActivity().onBackPressed();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}
