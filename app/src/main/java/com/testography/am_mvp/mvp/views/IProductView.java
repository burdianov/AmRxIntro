package com.testography.am_mvp.mvp.views;

import com.testography.am_mvp.data.storage.dto.ProductDto;

public interface IProductView extends IView {
    void showProductView(ProductDto product);

    void updateProductCountView(ProductDto product);

}
