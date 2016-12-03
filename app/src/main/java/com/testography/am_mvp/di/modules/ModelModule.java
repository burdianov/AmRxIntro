package com.testography.am_mvp.di.modules;

import com.testography.am_mvp.data.managers.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Provides
    @Singleton
    DataManager privateDataManager() {
        return new DataManager();
    }

}
