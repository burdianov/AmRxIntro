package com.testography.am_mvp.ui.screens.catalog;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testography.am_mvp.R;
import com.testography.am_mvp.data.storage.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

import mortar.MortarScope;

public class CatalogAdapter extends PagerAdapter {

    public static final String TAG = "CatalogAdapter";

    private List<ProductDto> mProductList = new ArrayList<>();

    public CatalogAdapter() {

    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void addItem(ProductDto product) {
        mProductList.add(product);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ProductDto product = mProductList.get(position);
        Context productContext = CatalogScreen.Factory.createProductContext
                (product, container.getContext());
        View newView = LayoutInflater.from(productContext).inflate(R.layout
                .screen_product, container, false);
        container.addView(newView);
        return newView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        MortarScope screenScope = MortarScope.getScope(((View) object).getContext());
        container.removeView((View) object);
        screenScope.destroy();
        Log.e(TAG, "destroyItem having the name: " + screenScope.getName());
    }
}
