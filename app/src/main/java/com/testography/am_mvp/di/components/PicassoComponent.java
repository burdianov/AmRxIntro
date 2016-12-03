package com.testography.am_mvp.di.components;

import com.squareup.picasso.Picasso;
import com.testography.am_mvp.di.modules.PicassoCacheModule;
import com.testography.am_mvp.di.scopes.RootScope;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PicassoCacheModule.class)
@RootScope
public interface PicassoComponent {
    Picasso getPicasso();
}
