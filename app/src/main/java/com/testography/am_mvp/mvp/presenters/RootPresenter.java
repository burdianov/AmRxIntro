package com.testography.am_mvp.mvp.presenters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.testography.am_mvp.App;
import com.testography.am_mvp.data.storage.dto.ActivityResultDto;
import com.testography.am_mvp.data.storage.dto.UserInfoDto;
import com.testography.am_mvp.mvp.models.AccountModel;
import com.testography.am_mvp.mvp.views.IRootView;
import com.testography.am_mvp.ui.activities.RootActivity;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class RootPresenter extends AbstractPresenter<IRootView> {

    private PublishSubject<ActivityResultDto> mActivityResultDtoObs = PublishSubject.create();

    @Inject
    AccountModel mAccountModel;

    public RootPresenter() {
        App.getRootActivityRootComponent().inject(this);
    }

    public PublishSubject<ActivityResultDto> getActivityResultDtoObs() {
        return mActivityResultDtoObs;
    }

    @Override
    public void initView() {
        mAccountModel.getUserInfoObs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserInfoSubscriber());
    }

    @RxLogSubscriber
    private class UserInfoSubscriber extends Subscriber<UserInfoDto> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (getView() != null) {
                getView().showError(e);
            }
        }

        @Override
        public void onNext(UserInfoDto userInfoDto) {
            if (getView() != null) {
                getView().initDrawer(userInfoDto);
            }
        }
    }

    public boolean checkPermissionsAndRequestIfNotGranted(
            @NonNull String[] permissions, int requestCode) {

        boolean allGranted = true;
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission(((RootActivity) getView()), permission);
            if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((RootActivity) getView()).requestPermissions(permissions, requestCode);
            }
            return false;
        }
        return allGranted;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mActivityResultDtoObs.onNext(new ActivityResultDto(requestCode,
                resultCode, intent));
        // TODO: 06-Dec-16 get result from RootActivity
    }

    // TODO: 06-Dec-16 the following method shall be verified
    public void onRequestPermissionResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {

    }
}
