package com.testography.am_mvp.mvp.views;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.testography.am_mvp.ui.activities.RootActivity;

public interface IRootView extends IView {
    void showMessage(String message);
    void showError(Throwable e);

    void showLoad();
    void hideLoad();

//    void setTypeface();

    @Nullable
    IView getCurrentScreen();

    void loadPhotoFromCamera();

    void loadPhotoFromGallery();

    Uri getPhoto();

    void setAvatarCallback(RootActivity.AvatarHolderCallback avatarCallback);
}
