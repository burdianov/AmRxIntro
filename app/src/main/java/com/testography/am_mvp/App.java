package com.testography.am_mvp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.components.AppComponent;
import com.testography.am_mvp.di.components.DaggerAppComponent;
import com.testography.am_mvp.di.modules.AppModule;
import com.testography.am_mvp.di.modules.PicassoCacheModule;
import com.testography.am_mvp.di.modules.RootModule;
import com.testography.am_mvp.mortar.ScreenScoper;
import com.testography.am_mvp.ui.activities.DaggerRootActivity_RootComponent;
import com.testography.am_mvp.ui.activities.RootActivity;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class App extends Application {
    private static SharedPreferences sSharedPreferences;
    private static Context sAppContext;
    private MortarScope mRootActivityScope;
    private static RootActivity.RootComponent mRootActivityRootComponent;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    private static AppComponent sAppComponent;
    private MortarScope mRootScope;

    @Override
    public Object getSystemService(String name) {
        return mRootScope.hasService(name) ? mRootScope.getService(name) : super
                .getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createAppComponent();
        createRootActivityComponent();

        mRootScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, sAppComponent)
                .build("Root");

        mRootActivityScope = mRootScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, mRootActivityRootComponent)
                .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                .build(RootActivity.class.getName());

        ScreenScoper.registerScope(mRootScope);
        ScreenScoper.registerScope(mRootActivityScope);

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sAppContext = getApplicationContext();
    }

    private void createAppComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    private void createRootActivityComponent() {
        mRootActivityRootComponent = DaggerRootActivity_RootComponent.builder()
                .appComponent(sAppComponent)
                .rootModule(new RootModule())
                .picassoCacheModule(new PicassoCacheModule())
                .build();
    }

    public static RootActivity.RootComponent getRootActivityRootComponent() {
        return mRootActivityRootComponent;
    }
}
