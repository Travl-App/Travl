package com.travl.guide.di.modules;

import com.travl.guide.mvp.model.network.NetService;
import com.travl.guide.mvp.model.repo.ArticlesRepo;

import dagger.Module;
import dagger.Provides;

@Module
public class ArticlesRepoModule {
    @Provides
    public ArticlesRepo getArticlesRepo(NetService netService) {
        return new ArticlesRepo(netService);
    }
}
