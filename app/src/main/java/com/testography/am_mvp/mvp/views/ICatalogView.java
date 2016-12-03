package com.testography.am_mvp.mvp.views;

import com.testography.am_mvp.data.storage.dto.ProductDto;

import java.util.List;

public interface ICatalogView extends IView {
    void showCatalogView(List<ProductDto> productsList);
    void updateProductCounter();
}
