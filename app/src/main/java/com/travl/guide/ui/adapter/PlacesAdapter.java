package com.travl.guide.ui.adapter;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.list.PlacePresenter;
import com.travl.guide.mvp.view.list.PlacesItemView;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private PlacePresenter presenter;
    private IImageLoader imageLoader;

    public PlacesAdapter(PlacePresenter presenter, IImageLoader imageLoader) {
        this.presenter = presenter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlacesViewHolder((MaterialCardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_places, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        RxView.clicks(holder.itemView).map(obj -> holder).subscribe(presenter.getClickSubject());
        holder.position = position;
        presenter.bindView(holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getListCount();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements PlacesItemView {

        int position = 0;
        private MaterialCardView cardView;

        PlacesViewHolder(@NonNull MaterialCardView cardView) {
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
