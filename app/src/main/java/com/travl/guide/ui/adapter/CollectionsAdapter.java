package com.travl.guide.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.travl.guide.R;
import com.travl.guide.mvp.presenter.list.CollectionPresenter;
import com.travl.guide.mvp.view.list.CollectionsItemView;

//Created by Pereved on 23.02.2019.
public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionsViewHolder> {

    private CollectionPresenter presenter;

    public CollectionsAdapter(CollectionPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CollectionsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collections, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int position) {
        RxView.clicks(holder.itemView).map(obj -> holder).subscribe(presenter.getClickSubject());
        holder.position = position;
        presenter.bindView(holder);
    }

    @Override
    public int getItemCount() {
//        return presenter.getListCount();
        return 20;
    }

    public class CollectionsViewHolder extends RecyclerView.ViewHolder implements CollectionsItemView {

        int position = 0;

        CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void setImage(String url) {

        }

        @Override
        public void setTittle(String tittle) {

        }

        @Override
        public int getPos() {
            return position;
        }
    }
}
