package com.testography.am_mvp.di.components;

import com.testography.am_mvp.data.managers.DataManager;
import com.testography.am_mvp.di.modules.LocalModule;
import com.testography.am_mvp.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class,
        modules = {NetworkModule.class, LocalModule.class})
@Singleton
public interface DataManagerComponent {
    void inject(DataManager dataManager);
}
