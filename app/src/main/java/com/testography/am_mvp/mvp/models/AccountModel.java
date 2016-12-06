package com.testography.am_mvp.mvp.models;

import android.net.Uri;

import com.testography.am_mvp.data.storage.dto.UserAddressDto;
import com.testography.am_mvp.data.storage.dto.UserDto;
import com.testography.am_mvp.data.storage.dto.UserSettingsDto;

import java.util.ArrayList;
import java.util.Map;

import rx.Observable;

import static com.testography.am_mvp.data.managers.PreferencesManager.NOTIFICATION_ORDER_KEY;
import static com.testography.am_mvp.data.managers.PreferencesManager.NOTIFICATION_PROMO_KEY;

public class AccountModel extends AbstractModel {

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

    public UserDto getUserDto() {
        return null;
//        return new UserDto(getUserProfileInfo(), getUserAddresses(), getUserSettings());
    }

    private Map<String, String> getUserProfileInfo() {
        return mDataManager.getUserProfileInfo();
    }

    public void saveProfileInfo(String name, String phone) {
        mDataManager.saveProfileInfo(name, phone);
    }

    public void saveAvatarPhoto(Uri photoUri) {
        mDataManager.saveAvatarPhoto(photoUri);
    }

    public void savePromoNotification(boolean isChecked) {
        mDataManager.saveSetting(NOTIFICATION_PROMO_KEY, isChecked);
    }

    public void saveOrderNotification(boolean isChecked) {
        mDataManager.saveSetting(NOTIFICATION_ORDER_KEY,
                isChecked);
    }

    public void addAddress(UserAddressDto userAddressDto) {
        mDataManager.addAddress(userAddressDto);
    }

    // TODO: 29-Nov-16 remove address
}
