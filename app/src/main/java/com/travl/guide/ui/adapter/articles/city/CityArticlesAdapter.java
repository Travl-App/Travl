package com.travl.guide.ui.adapter.articles.city;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.list.CityArticlesListPresenter;
import com.travl.guide.mvp.view.articles.list.CityArticlesItemView;

public class CityArticlesAdapter extends RecyclerView.Adapter<CityArticlesAdapter.CityArticlesViewHolder> {
    private CityArticlesListPresenter presenter;
    private IImageLoader imageLoader;

    public CityArticlesAdapter(CityArticlesListPresenter presenter, IImageLoader imageLoader) {
        this.presenter = presenter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public CityArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CityArticlesViewHolder((MaterialCardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_article_preview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CityArticlesViewHolder cityArticlesViewHolder, int position) {
        RxView.clicks(cityArticlesViewHolder.itemView).map(obj -> cityArticlesViewHolder).subscribe(presenter.getClickSubject(position));
        cityArticlesViewHolder.position = position;
        presenter.bindView(cityArticlesViewHolder);
    }

    @Override
    public int getItemCount() {
        return presenter.getListCount();
    }

    public class CityArticlesViewHolder extends RecyclerView.ViewHolder implements CityArticlesItemView {

        int position = 0;
        private MaterialCardView cardView;

        CityArticlesViewHolder(@NonNull MaterialCardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }

        @Override
        public void setImage(String url) {
            imageLoader.loadInto(cardView.findViewById(R.id.city_article_preview_image_view), url);
        }

        @Override
        public void setDescription(String description) {
            ((TextView) cardView.findViewById(R.id.city_article_preview_description)).setText(description);
        }

        @Override
        public int getPos() {
            return position;
        }
    }
}
