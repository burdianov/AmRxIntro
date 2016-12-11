package com.testography.am_mvp.mvp.models;

import com.testography.am_mvp.data.storage.dto.UserAddressDto;
import com.testography.am_mvp.data.storage.dto.UserInfoDto;
import com.testography.am_mvp.data.storage.dto.UserSettingsDto;

import java.util.ArrayList;
import java.util.Map;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static com.testography.am_mvp.data.managers.PreferencesManager.NOTIFICATION_ORDER_KEY;
import static com.testography.am_mvp.data.managers.PreferencesManager.NOTIFICATION_PROMO_KEY;
import static com.testography.am_mvp.data.managers.PreferencesManager.PROFILE_AVATAR_KEY;
import static com.testography.am_mvp.data.managers.PreferencesManager.PROFILE_FULL_NAME_KEY;
import static com.testography.am_mvp.data.managers.PreferencesManager.PROFILE_PHONE_KEY;

public class AccountModel extends AbstractModel {

    //    private PublishSubject<UserInfoDto> mUserInfoObs = PublishSubject.create();
    private BehaviorSubject<UserInfoDto> mUserInfoObs = BehaviorSubject.create();

    public AccountModel() {
        mUserInfoObs.onNext(getUserProfileInfo());
    }

    //region ==================== Addresses ===================

    public Observable<UserAddressDto> getAddressObs() {
        return Observable.from(getUserAddresses());
    }

    private ArrayList<UserAddressDto> getUserAddresses() {
        return mDataManager.getUserAddresses();
    }

    public void updateOrInsertAddress(UserAddressDto addressDto) {
        mDataManager.updateOrInsertAddress(addressDto);
    }

    public void removeAddress(UserAddressDto addressDto) {
        mDataManager.removeAddress(addressDto);
    }

    public UserAddressDto getAddressFromPosition(int position) {
        return getUserAddresses().get(position);
    }

    //endregion

    //region ==================== Settings ===================

    public Observable<UserSettingsDto> getUserSettingsObs() {
        return Observable.just(getUserSettings());
    }

    private UserSettingsDto getUserSettings() {
        Map<String, Boolean> map = mDataManager.getUserSettings();
        return new UserSettingsDto(map.get(NOTIFICATION_ORDER_KEY), map.get
                (NOTIFICATION_PROMO_KEY));
    }

    public void saveSettings(UserSettingsDto settings) {
        mDataManager.saveSetting(NOTIFICATION_ORDER_KEY, settings.isOrderNotification());
        mDataManager.saveSetting(NOTIFICATION_PROMO_KEY, settings.isPromoNotification());
    }

    //endregion

    //region ==================== User ===================

    public void saveProfileInfo(UserInfoDto userInfo) {
        mDataManager.saveProfileInfo(userInfo.getName(), userInfo.getPhone(),
                userInfo.getAvatar());
        mUserInfoObs.onNext(userInfo);
    }

    public UserInfoDto getUserProfileInfo() {
        Map<String, String> map = mDataManager.getUserProfileInfo();
        return new UserInfoDto(
                map.get(PROFILE_FULL_NAME_KEY),
                map.get(PROFILE_PHONE_KEY),
                map.get(PROFILE_AVATAR_KEY));
    }

    public Observable<UserInfoDto> getUserInfoObs() {
        return mUserInfoObs;
    }

    //endregion
}
