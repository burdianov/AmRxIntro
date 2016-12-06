package com.testography.am_mvp.mvp.presenters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.testography.am_mvp.data.storage.dto.ActivityResultDto;
import com.testography.am_mvp.mvp.views.IRootView;
import com.testography.am_mvp.ui.activities.RootActivity;
import com.testography.am_mvp.utils.ConstantsManager;

import rx.subjects.PublishSubject;

public class RootPresenter extends AbstractPresenter<IRootView> {

    private PublishSubject<ActivityResultDto> mActivityResultDtoObs = PublishSubject.create();

    public PublishSubject<ActivityResultDto> getActivityResultDtoObs() {
        return mActivityResultDtoObs;
    }

    @Override
    public void initView() {
        // TODO: 04-Nov-16 init drawer avatar + username
    }

    public boolean checkPermissionsAndRequestIfNotGranted(
            @NonNull String[] permissions, int requestCode) {

        boolean allGranted = true;
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission((
                    (RootActivity) getView()), permission);
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

        if (requestCode == ConstantsManager.REQUEST_PERMISSION_CAMERA &&
                grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
        if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // TODO: process other permission

        } else {

        }
    }
}
