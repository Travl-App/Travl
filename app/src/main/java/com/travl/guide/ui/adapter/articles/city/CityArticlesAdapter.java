package com.travl.guide.ui.adapter.articles.city;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CityArticlesViewHolder cityArticlesViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
            imageLoader.loadInto(cardView.findViewById(R.id.place_image_view), url);
        }

        @Override
        public void setDescription(String description) {
            ((TextView) cardView.findViewById(R.id.place_description)).setText(description);
        }

        @Override
        public int getPos() {
            return position;
        }
    }
}
