package com.testography.am_mvp.ui.screens.catalog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.testography.am_mvp.R;
import com.testography.am_mvp.data.storage.dto.ProductDto;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.di.scopes.CatalogScope;
import com.testography.am_mvp.flow.AbstractScreen;
import com.testography.am_mvp.flow.Screen;
import com.testography.am_mvp.mvp.models.CatalogModel;
import com.testography.am_mvp.mvp.presenters.ICatalogPresenter;
import com.testography.am_mvp.mvp.presenters.RootPresenter;
import com.testography.am_mvp.mvp.presenters.SubscribePresenter;
import com.testography.am_mvp.mvp.views.IRootView;
import com.testography.am_mvp.ui.activities.RootActivity;
import com.testography.am_mvp.ui.screens.auth.AuthScreen;
import com.testography.am_mvp.ui.screens.product.ProductScreen;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import rx.Subscription;

@Screen(R.layout.screen_catalog)
public class CatalogScreen extends AbstractScreen<RootActivity.RootComponent> {

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerCatalogScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    //region ==================== DI ===================

    @dagger.Module
    public class Module {
        @Provides
        @CatalogScope
        CatalogModel provideCatalogModel() {
            return new CatalogModel();
        }

        @Provides
        @CatalogScope
        CatalogPresenter provideCatalogPresenter() {
            return new CatalogPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules =
            Module.class)
    @CatalogScope
    public interface Component {
        void inject(CatalogPresenter presenter);

        void inject(CatalogView view);

        CatalogModel getCatalogModel();

        Picasso getPicasso();
    }

    //endregion

    //region ==================== Presenter ===================

    public class CatalogPresenter extends SubscribePresenter<CatalogView> implements
            ICatalogPresenter {

        @Inject
        RootPresenter mRootPresenter;
        @Inject
        CatalogModel mCatalogModel;

        private Subscription mProductListSub;

        //region ==================== Lifecycle ===================
        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            subscribeOnProductListObs();
        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);
            mProductListSub.unsubscribe();
        }

        //endregion

        //region ==================== Subscription ===================

        private void subscribeOnProductListObs() {

            mProductListSub = subscribe(mCatalogModel.getProductListObs(), new
                    ViewSubscriber<List<ProductDto>>() {
                        @Override
                        public void onNext(List<ProductDto> productList) {
                            if (getView() != null) {
                                getView().showCatalogView(productList);
                            }
                        }
                    });
        }

        //endregion

        @Override
        public void clickOnBuyButton(int position) {
            if (getView() != null) {
                if (checkUserAuth() && getRootView() != null) {
                    getRootView().showMessage("Item " + mCatalogModel.getProductList()
                            .get(position).getProductName() +
                            " added successfully to the Cart");
                } else {
                    Flow.get(getView()).set(new AuthScreen());
                }
            }
        }

        @Nullable
        public IRootView getRootView() {
            return mRootPresenter.getView();
        }

        @Override
        public boolean checkUserAuth() {
            return mCatalogModel.isUserAuth();
        }
    }

    //endregion

    public static class Factory {
        public static Context createProductContext(ProductDto product, Context
                parentContext) {
            MortarScope parentScope = MortarScope.getScope(parentContext);
            MortarScope childScope = null;
            ProductScreen screen = new ProductScreen(product);
            String scopeName = String.format("%s_%d", screen.getScopeName(),
                    product.getId());

            if (parentScope.findChild(scopeName) == null) {
                childScope = parentScope.buildChild()
                        .withService(DaggerService.SERVICE_NAME, screen
                                .createScreenComponent(DaggerService
                                        .<CatalogScreen.Component>getDaggerComponent
                                                (parentContext)))
                        .build(scopeName);
            } else {
                childScope = parentScope.findChild(scopeName);
            }
            return childScope.createContext(parentContext);
        }
    }
}
