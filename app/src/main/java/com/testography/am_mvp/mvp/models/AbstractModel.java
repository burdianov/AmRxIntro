package com.testography.am_mvp.mvp.models;

import com.testography.am_mvp.data.managers.DataManager;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.components.DaggerModelComponent;
import com.testography.am_mvp.di.components.ModelComponent;
import com.testography.am_mvp.di.modules.ModelModule;

import javax.inject.Inject;

public abstract class AbstractModel {

    @Inject
    DataManager mDataManager;

    public AbstractModel() {
        ModelComponent component = DaggerService.getComponent(ModelComponent.class);
        if (component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(ModelComponent.class, component);
        }
        component.inject(this);
    }

    private ModelComponent createDaggerComponent() {
        return DaggerModelComponent.builder()
                .modelModule(new ModelModule())
                .build();
    }
}