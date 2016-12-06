package com.testography.am_mvp.mvp.views;

import android.support.annotation.Nullable;

import com.testography.am_mvp.data.storage.dto.UserInfoDto;

public interface IRootView extends IView {
    void showMessage(String message);
    void showError(Throwable e);

    void showLoad();
    void hideLoad();

//    void setTypeface();

    @Nullable
    IView getCurrentScreen();

    void initDrawer(UserInfoDto userInfoDto);
}
