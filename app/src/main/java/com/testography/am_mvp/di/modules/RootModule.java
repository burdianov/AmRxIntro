package com.testography.am_mvp.di.modules;

import com.testography.am_mvp.di.scopes.RootScope;
import com.testography.am_mvp.mvp.presenters.RootPresenter;

import dagger.Provides;

@dagger.Module
public class RootModule {
    @Provides
    @RootScope
    RootPresenter provideRootPresenter() {
        return new RootPresenter();
    }
}
