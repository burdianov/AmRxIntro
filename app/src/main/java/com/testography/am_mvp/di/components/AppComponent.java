package com.testography.am_mvp.di.components;

import android.content.Context;

import com.testography.am_mvp.di.modules.AppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
