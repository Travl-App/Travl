package com.travl.guide.ui.adapter.articles.travlzine;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.jakewharton.rxbinding2.view.RxView;
import com.travl.guide.R;
import com.travl.guide.mvp.model.image.IImageLoader;
import com.travl.guide.mvp.presenter.articles.list.TravlZineArticlesListPresenter;
import com.travl.guide.mvp.view.articles.list.TravlZineArticlesItemView;
import com.travl.guide.mvp.view.articles.list.TravlZineFooterItemView;
import com.travl.guide.ui.App;

import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class TravlZineArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private TravlZineArticlesListPresenter presenter;
    private IImageLoader imageLoader;

    public TravlZineArticlesAdapter(TravlZineArticlesListPresenter presenter, IImageLoader imageLoader) {
        this.presenter = presenter;
        this.imageLoader = imageLoader;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == presenter.getListCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travlzine_articles_list_footer, viewGroup, false));
        } else {
            return new TravlZineArticlesViewHolder((MaterialCardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travlzine_article_preview, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Timber.e("position = " + position);
        if (holder instanceof TravlZineArticlesViewHolder) {
            TravlZineArticlesViewHolder travlZineArticlesViewHolder = (TravlZineArticlesViewHolder) holder;
            PublishSubject<TravlZineArticlesItemView> publishSubject = presenter.getClickSubject(position);
            RxView.clicks(travlZineArticlesViewHolder.itemView).map(obj -> travlZineArticlesViewHolder).subscribe(publishSubject);
            travlZineArticlesViewHolder.position = position;
            presenter.bindView(travlZineArticlesViewHolder);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            PublishSubject<TravlZineFooterItemView> publishSubject = presenter.getFooterClickSubject();
            RxView.clicks(footerViewHolder.loadMoreButton).map(obj -> footerViewHolder).subscribe(publishSubject);
            presenter.bindFooterView();
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getListCount() + 1;
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
        public void setCategory(String category) {
            Resources resources = App.getInstance().getResources();
            int categoryDrawable = R.drawable.ic_category_11;
            if (category.length() < 4) {
                categoryDrawable = R.drawable.ic_category_3;
            } else if (category.length() < 8) {
                categoryDrawable = R.drawable.ic_category_7;
            }
            ImageView categoryView = cardView.findViewById(R.id.travlzine_article_preview_category_image_view);
            VectorChildFinder vector = new VectorChildFinder(App.getInstance(), categoryDrawable, categoryView);
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName("path1");
            TextView categoryText = ((TextView) cardView.findViewById(R.id.travlzine_article_preview_category_text_view));
            categoryText.setText(category);
            Timber.e("Категория = " + category);
            int color = 0;
            category = category.toUpperCase();
            if (category.equals("АРХИТЕКТУРА")) {
                color = Color.parseColor(resources.getString(R.string.category_color_architecture));
            } else if (category.equals("ПОКУПКИ")) {
                color = Color.parseColor(resources.getString(R.string.category_color_goods));
            } else if (category.equals("ЕДА")) {
                color = Color.parseColor(resources.getString(R.string.category_color_food));
            } else if (category.equals("ЖИЛЬЕ") || category.equals("ОТЕЛЬ")) {
                color = Color.parseColor(resources.getString(R.string.category_color_habitation));
            } else if (category.equals("ПРИРОДА")) {
                color = Color.parseColor(resources.getString(R.string.category_color_nature));
            } else if (category.equals("STREETART")) {
                color = Color.parseColor(resources.getString(R.string.category_color_streetart));
            } else if (category.equals("НАХОДКА")) {
                color = Color.parseColor(resources.getString(R.string.category_color_trove));
            } else if (category.equals("URBEX")) {
                color = Color.parseColor(resources.getString(R.string.category_color_urbex));
            } else if (category.equals("ВИД")) {
                color = Color.parseColor(resources.getString(R.string.category_color_view));
            } else {
                color = Color.parseColor(resources.getString(R.string.category_color_default));
            }
            path1.setStrokeColor(color);
            categoryText.setTextColor(color);
            categoryView.invalidate();
        }

        @Override
        public void setDescription(String description) {
            ((TextView) cardView.findViewById(R.id.travlzine_article_preview_description)).setText(description);
        }

        @Override
        public void setAuthor(String author) {
            ((Chip) cardView.findViewById(R.id.travlzine_article_preview_author_chip)).setText(author);
        }

        @Override
        public int getPos() {
            return position;
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder implements TravlZineFooterItemView {
        Button loadMoreButton;

        FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            Timber.e("FooterViewHolder");
            loadMoreButton = itemView.findViewById(R.id.travlzine_articles_list_footer_button);
        }

        @Override
        public void loadMoreArticles() {
            Timber.e("Loading more articles");
            Toast.makeText(App.getInstance(), "Loading more articles", Toast.LENGTH_SHORT).show();
            presenter.loadMoreArticles();
        }
    }
}
