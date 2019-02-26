package com.travl.guide.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.list.PlacePresenter;
import com.travl.guide.mvp.view.list.PlacesItemView;

//Created by Pereved on 23.02.2019.
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private PlacePresenter presenter;

    public PlacesAdapter(PlacePresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlacesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collections, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        RxView.clicks(holder.itemView).map(obj -> holder).subscribe(presenter.getClickSubject());
        holder.position = position;
        presenter.bindView(holder);
    }

    @Override
    public int getItemCount() {
//        return presenter.getListCount();
        return 20;
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements PlacesItemView {

        int position = 0;

        PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void setImage(String url) {

        }

        @Override
        public void setTitle(String title) {

        }

        @Override
        public int getPos() {
            return position;
        }
    }
}
