package com.testography.am_mvp.mvp.models;

import com.testography.am_mvp.data.storage.dto.ProductDto;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class CatalogModel extends AbstractModel {

    private BehaviorSubject<List<ProductDto>> mProductListObs = BehaviorSubject
            .create();

    public CatalogModel() {
        mProductListObs.onNext(getProductList());
    }

    //region ==================== Products ===================

    public Observable<List<ProductDto>> getProductListObs() {
        return mProductListObs;
    }

    public List<ProductDto> getProductList() {
        return mDataManager.getProductList();
    }

    //endregion

    public boolean isUserAuth() {
        return mDataManager.isAuthUser();
    }

    public ProductDto getProductById(int productId) {
        // TODO: 28-Oct-16 get product from datamanager
        return mDataManager.getProductById(productId);
    }

    public void updateProduct(ProductDto product) {
        mDataManager.updateProduct(product);
    }
}
