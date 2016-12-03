package com.testography.am_mvp.mvp.views;

import com.testography.am_mvp.data.storage.dto.UserAddressDto;

public interface IAddressView extends IView {
    void showInputError();

    UserAddressDto getUserAddress();
}
