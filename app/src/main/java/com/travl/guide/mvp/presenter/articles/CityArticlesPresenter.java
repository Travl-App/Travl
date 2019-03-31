package com.travl.guide.mvp.presenter.articles;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.presenter.articles.list.CityArticlesListPresenter;
import com.travl.guide.mvp.view.articles.CityArticlesView;
import com.travl.guide.mvp.view.articles.list.CityArticlesItemView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@InjectViewState
public class CityArticlesPresenter extends MvpPresenter<CityArticlesView> {


    private CityArticlesListPresenterImpl cityArticlesListPresenter;
    @Inject
    ArticlesRepo articlesRepo;
    @Inject
    @Named("baseUrl")
    String baseUrl;
    private Scheduler scheduler;

    public CityArticlesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setArticlesList(List<ArticleLink> articlesList) {
        cityArticlesListPresenter.setArticleLinkList(articlesList);
    }

    public class CityArticlesListPresenterImpl implements CityArticlesListPresenter {
        PublishSubject<CityArticlesItemView> clickSubject = PublishSubject.create();
        private List<ArticleLink> articleLinkList;

        @Override
        public PublishSubject<CityArticlesItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(CityArticlesItemView view) {
            Timber.d("BindView and set Description");
            ArticleLink articleLink = articleLinkList.get(view.getPos());
            view.setDescription(articleLink.getTitle());
            view.setImage(baseUrl + articleLink.getImageCoverUrl().substring(1));
        }

        @Override
        public int getListCount() {
            Timber.d("PlaceList size = %s", (articleLinkList == null ? null : articleLinkList.size()));
            return articleLinkList == null ? 0 : articleLinkList.size();
        }

        @Override
        public void setArticleLinkList(List<ArticleLink> articleLinks) {
            this.articleLinkList = articleLinks;
            if (articleLinkList == null || articleLinkList.size() == 0) {
                this.articleLinkList = new ArrayList<>();
                this.articleLinkList.add(new ArticleLink("Не найдено статей по выбранному городу", "/media/article_cover/vlNEiCnDa4bIYc9QZAG3cQ.jpg"));
            }
            getViewState().onChangedArticlesData();
        }
    }

    public CityArticlesListPresenterImpl getCityArticlesListPresenter() {
        if (cityArticlesListPresenter == null)
            cityArticlesListPresenter = new CityArticlesListPresenterImpl();
        return cityArticlesListPresenter;
    }
}
