package com.testography.am_mvp.mvp.presenters;

import android.net.Uri;

public interface IAccountPresenter {
    void onClickAddress();

    void switchViewState();

    void switchOrder(boolean isChecked);

    void switchPromo(boolean isChecked);

    void takePhoto();

    void chooseCamera();

    void chooseGallery();

    Uri getSelectedImage();

    void setAvatar(Uri avatar);
}
