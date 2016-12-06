package com.testography.am_mvp.ui.screens.account;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.testography.am_mvp.R;
import com.testography.am_mvp.data.storage.dto.UserAddressDto;
import com.testography.am_mvp.data.storage.dto.UserSettingsDto;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.scopes.AccountScope;
import com.testography.am_mvp.flow.AbstractScreen;
import com.testography.am_mvp.flow.Screen;
import com.testography.am_mvp.mvp.models.AccountModel;
import com.testography.am_mvp.mvp.presenters.IAccountPresenter;
import com.testography.am_mvp.mvp.presenters.RootPresenter;
import com.testography.am_mvp.mvp.presenters.SubscribePresenter;
import com.testography.am_mvp.mvp.views.IRootView;
import com.testography.am_mvp.ui.activities.RootActivity;
import com.testography.am_mvp.ui.screens.address.AddressScreen;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Subscription;

@Screen(R.layout.screen_account)
public class AccountScreen extends AbstractScreen<RootActivity.RootComponent> {

    private int mCustomState = 1;

    public int getCustomState() {
        return mCustomState;
    }

    public void setCustomState(int customState) {
        mCustomState = customState;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAccountScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    //region ==================== DI ===================
    @dagger.Module
    public class Module {
        @Provides
        @AccountScope
        AccountModel provideAccountModel() {
            return new AccountModel();
        }

        @Provides
        @AccountScope
        AccountPresenter provideAccountPresenter() {
            return new AccountPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules =
            Module.class)
    @AccountScope
    public interface Component {
        void inject(AccountPresenter presenter);

        void inject(AccountView view);

        RootPresenter getRootPresenter();

        AccountModel getAccountModel();
    }
    //endregion


    //region ==================== Presenter ===================
    public class AccountPresenter extends SubscribePresenter<AccountView> implements
            IAccountPresenter {

        public static final String TAG = "AccountPresenter";

        @Inject
        RootPresenter mRootPresenter;
        @Inject
        AccountModel mAccountModel;

        private Uri mAvatarUri;
        private Subscription mAddressSub;
        private Subscription mSettingsSub;

        //region ==================== Lifecycle ===================

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null) {
                getView().initView();
            }
            subscribeOnAddressesObs();
            subscribeOnSettingsObs();
        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);
            mAddressSub.unsubscribe();
            mSettingsSub.unsubscribe();
        }

        @Override
        protected void onExitScope() {
            super.onExitScope();
        }

        //endregion

        //region ==================== Subscription ===================

        private void subscribeOnAddressesObs() {

            mAddressSub = subscribe(mAccountModel.getAddressObs(), new
                    ViewSubscriber<UserAddressDto>() {
                        @Override
                        public void onNext(UserAddressDto addressDto) {
                            if (getView() != null) {
                                getView().getAdapter().addItem(addressDto);
                            }
                        }
                    });
        }

        public void updateListView() {
            getView().getAdapter().reloadAdapter();
            subscribeOnAddressesObs();
        }

        private void subscribeOnSettingsObs() {
            mSettingsSub = subscribe(mAccountModel.getUserSettingsObs(), new ViewSubscriber<UserSettingsDto>() {
                @Override
                public void onNext(UserSettingsDto userSettingsDto) {
                    if (getView() != null) {
                        getView().initSettings(userSettingsDto);
                    }
                }
            });
        }

        //endregion

        @Override
        public void clickOnAddress() {
            Flow.get(getView()).set(new AddressScreen(null));
        }

        @Override
        public void switchViewState() {
            if (getCustomState() == AccountView.EDIT_STATE && getView() != null) {
//                mAccountModel.saveProfileInfo(getView().getUserName(), getView()
//                        .getUserPhone());
//                mAccountModel.saveAvatarPhoto(mAvatarUri);
            }
            if (getView() != null) {
                getView().changeState();
            }
        }

        @Override
        public void switchOrder(boolean isChecked) {
            mAccountModel.saveOrderNotification(isChecked);
        }

        @Override
        public void switchPromo(boolean isChecked) {
            mAccountModel.savePromoNotification(isChecked);
        }

        @Override
        public void takePhoto() {
            if (getView() != null) {
                getView().showPhotoSourceDialog();
            }
        }

        @Override
        public void chooseCamera() {
            if (getView() != null) {
                getRootView().loadPhotoFromCamera();
            }
        }

        @Override
        public void chooseGallery() {
            if (getView() != null) {
                getRootView().loadPhotoFromGallery();
            }
        }

        @Override
        public void removeAddress(int position) {
            mAccountModel.removeAddress(mAccountModel.getAddressFromPosition(position));
            updateListView();
        }

        @Override
        public void editAddress(int position) {
            Flow.get(getView()).set(new AddressScreen(mAccountModel
                    .getAddressFromPosition(position)));
        }

        @Nullable
        @Override
        protected IRootView getRootView() {
            return mRootPresenter.getView();
        }

        public void switchSettings() {
            if (getView() != null) {
                mAccountModel.saveSettings(getView().getSettings());
            }
        }
    }
    //endregion
}
