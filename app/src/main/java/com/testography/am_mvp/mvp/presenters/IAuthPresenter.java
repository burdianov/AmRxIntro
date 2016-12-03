package com.testography.am_mvp.mvp.presenters;

public interface IAuthPresenter {

    void clickOnLogin();
    void clickOnFb();
    void clickOnVk();
    void clickOnTwitter();
    void clickOnShowCatalog();

    boolean checkUserAuth();
}
