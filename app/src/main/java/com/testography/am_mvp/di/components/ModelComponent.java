package com.testography.am_mvp.di.components;

import com.testography.am_mvp.di.modules.ModelModule;
import com.testography.am_mvp.mvp.models.AbstractModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel abstractModel);
}
