package com.travl.guide.mvp.model.repo;

import com.travl.guide.mvp.model.api.articles.Articles;
import com.travl.guide.mvp.model.network.NetService;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ArticlesRepo {
    private NetService netService;

    public ArticlesRepo(NetService netService) {
        this.netService = netService;
    }

    public Single<Articles> getArticles() {
        return netService.getArticles().subscribeOn(Schedulers.io());
    }
}
