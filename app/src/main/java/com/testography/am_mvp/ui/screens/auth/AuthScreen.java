package com.testography.am_mvp.ui.screens.auth;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.testography.am_mvp.R;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.scopes.AuthScope;
import com.testography.am_mvp.flow.AbstractScreen;
import com.testography.am_mvp.flow.Screen;
import com.testography.am_mvp.mvp.models.AuthModel;
import com.testography.am_mvp.mvp.presenters.IAuthPresenter;
import com.testography.am_mvp.mvp.presenters.RootPresenter;
import com.testography.am_mvp.mvp.views.IRootView;
import com.testography.am_mvp.ui.activities.RootActivity;
import com.testography.am_mvp.ui.activities.SplashActivity;
import com.testography.am_mvp.utils.CredentialsValidator;

import javax.inject.Inject;

import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;

@Screen(R.layout.screen_auth)
public class AuthScreen extends AbstractScreen<RootActivity.RootComponent> {

    private int mCustomState = 1;

    public void setCustomState(int customState) {
        mCustomState = customState;
    }

    public int getCustomState() {
        return mCustomState;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentRootComponent) {
        return DaggerAuthScreen_Component.builder()
                .rootComponent(parentRootComponent)
                .module(new Module())
                .build();
    }

    //region ==================== DI ===================
    @dagger.Module
    public class Module {

        @Provides
        @AuthScope
        AuthPresenter providePresenter() {
            return new AuthPresenter();
        }

        @Provides
        @AuthScope
        AuthModel provideAuthModel() {
            return new AuthModel();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules =
            Module.class)
    @AuthScope
    public interface Component {
        void inject(AuthPresenter presenter);
        void inject(AuthView view);
    }
    //endregion

    //region ==================== Presenter ===================
    public class AuthPresenter extends ViewPresenter<AuthView> implements IAuthPresenter {

        @Inject
        AuthModel mAuthModel;
        @Inject
        RootPresenter mRootPresenter;

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            if (getView() != null) {
                if (checkUserAuth()) {
                    getView().hideLoginBtn();
                } else {
                    getView().showLoginBtn();
                }
                getView().setTypeface();
            }
        }

        @Nullable
        private IRootView getRootView() {
            return mRootPresenter.getView();
        }

        @Override
        public void clickOnLogin() {
            if (getView() != null && getRootView() != null) {
                if (getView().isIdle()) {
                    getView().setCustomState(AuthView.LOGIN_STATE);
                } else {
                    String email = getView().getUserEmail();
                    String password = getView().getUserPassword();

                    if (!CredentialsValidator.isValidEmail(email)) {
                        getView().requestEmailFocus();
//                    getView().showMessage(sAppContext.getString(R.string.err_msg_email));
                        return;
                    }
                    if (!CredentialsValidator.isValidPassword(password)) {
                        getView().requestPasswordFocus();
//                    getView().showMessage(sAppContext.getString(R.string.err_msg_password));
                        return;
                    }
                    mAuthModel.loginUser(email, password);
                    getRootView().showLoad();
                    getRootView().showMessage("request for user auth");

                    Handler handler = new Handler();
                    handler.postDelayed(() -> getRootView().hideLoad(), 3000);
                }
            }
        }

        @Override
        public void clickOnFb() {
            if (getRootView() != null) {
                getRootView().showMessage("clickOnFb");
            }
        }

        @Override
        public void clickOnVk() {
            if (getRootView() != null) {
                getRootView().showMessage("clickOnVk");
            }
        }

        @Override
        public void clickOnTwitter() {
            if (getRootView() != null) {
                getRootView().showMessage("clickOnTwitter");
            }
        }

        @Override
        public void clickOnShowCatalog() {
            if (getView() != null && getRootView() != null) {
                getRootView().showMessage("Show catalog");

                if (getRootView() instanceof SplashActivity) {
                    ((SplashActivity) getRootView()).startRootActivity();
                } else {
                    // TODO: 27-Nov-16 show catalog screen
                }
            }
        }

        @Override
        public boolean checkUserAuth() {
            return mAuthModel.isAuthUser();
        }
    }
    //endregion


}
