package com.testography.am_mvp.mvp.models;

public class AuthModel extends AbstractModel {

//    private DataManager mDataManager;

    public AuthModel() {
//        mDataManager = DataManager.getInstance();
    }

    public boolean isAuthUser() {
        return mDataManager.isAuthUser();
    }

    public void loginUser(String email, String password) {
        mDataManager.loginUser(email, password);
    }
}
