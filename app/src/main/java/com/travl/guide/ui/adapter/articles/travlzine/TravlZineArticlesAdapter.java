package com.travl.guide.ui.adapter.articles.travlzine;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.list.TravlZineArticlesListPresenter;
import com.travl.guide.mvp.view.articles.list.TravlZineArticlesItemView;
import com.travl.guide.ui.App;

import timber.log.Timber;

public class TravlZineArticlesAdapter extends RecyclerView.Adapter<TravlZineArticlesAdapter.TravlZineArticlesViewHolder> {

    private TravlZineArticlesListPresenter presenter;
    private IImageLoader imageLoader;

    public TravlZineArticlesAdapter(TravlZineArticlesListPresenter presenter, IImageLoader imageLoader) {
        this.presenter = presenter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public TravlZineArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TravlZineArticlesViewHolder((MaterialCardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_travlzine_article_preview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TravlZineArticlesViewHolder holder, int position) {
        Timber.e("position = " + position);
        RxView.clicks(holder.itemView).map(obj -> holder).subscribe(presenter.getClickSubject(position));
        holder.position = position;
        presenter.bindView(holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getListCount();
    }

    public class TravlZineArticlesViewHolder extends RecyclerView.ViewHolder implements TravlZineArticlesItemView {

        int position = 0;
        private MaterialCardView cardView;

        TravlZineArticlesViewHolder(@NonNull MaterialCardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }

        @Override
        public void setImage(String url) {
            if (url != null) {
                imageLoader.loadInto(cardView.findViewById(R.id.travlzine_article_preview_image_view), url);
            } else {
                imageLoader.loadInto(cardView.findViewById(R.id.travlzine_article_preview_image_view), App.getInstance().getResources().getDrawable(R.drawable.ic_under_maintenance));
            }
        }

        @Override
        public void setDescription(String description) {
            ((TextView) cardView.findViewById(R.id.travlzine_article_preview_description)).setText(description);
        }

        @Override
        public int getPos() {
            return position;
        }
    }
}
