package com.travl.guide.mvp.presenter.articles;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.travl.guide.mvp.model.api.articles.ArticleLink;
import com.travl.guide.mvp.model.repo.ArticlesRepo;
import com.travl.guide.mvp.presenter.articles.list.CityArticlesListPresenter;
import com.travl.guide.mvp.view.articles.CityArticlesView;
import com.travl.guide.mvp.view.articles.list.CityArticlesItemView;
import com.travl.guide.navigator.Screens;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class CityArticlesPresenter extends MvpPresenter<CityArticlesView> {


    private CityArticlesListPresenterImpl cityArticlesListPresenter;
    @Inject
    ArticlesRepo articlesRepo;
    @Inject
    @Named("baseUrl")
    String baseUrl;
    @Inject
    Router router;
    private Scheduler scheduler;

    public CityArticlesPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setArticlesList(List<ArticleLink> articlesList) {
        cityArticlesListPresenter.setArticleLinkList(articlesList);
    }

    public class CityArticlesListPresenterImpl implements CityArticlesListPresenter {
        private List<ArticleLink> articleLinkList;
        private List<PublishSubject<CityArticlesItemView>> publishSubjectList;

        private void createPublishSubjects() {
            int elementsToUpdate = 0;
            if (publishSubjectList == null) {
                publishSubjectList = new ArrayList<>();
                elementsToUpdate = getListCount();
            } else {
                elementsToUpdate = getListCount() - publishSubjectList.size();
            }
            for (int i = 0; i < elementsToUpdate; i++) {
                publishSubjectList.add(PublishSubject.create());
            }
        }

        @Override
        public PublishSubject<CityArticlesItemView> getClickSubject(int position) {
            return publishSubjectList.get(position);
        }

        @SuppressLint("CheckResult")
        @Override
        public void bindView(CityArticlesItemView view) {
            Timber.d("BindView and set Description");
            int position = view.getPos();
            ArticleLink articleLink = articleLinkList.get(position);
            publishSubjectList.get(position).subscribe(cityArticlesItemView -> router.navigateTo(new Screens.ArticleScreen(articleLink.getLink())), Timber::e);
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
            }
            createPublishSubjects();
            getViewState().onChangedArticlesData();
        }
    }

    public CityArticlesListPresenterImpl getCityArticlesListPresenter() {
        if (cityArticlesListPresenter == null)
            cityArticlesListPresenter = new CityArticlesListPresenterImpl();
        return cityArticlesListPresenter;
    }
}
