package com.testography.am_mvp.ui.screens.product;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.testography.am_mvp.R;
import com.testography.am_mvp.data.storage.dto.ProductDto;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.mvp.views.IProductView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductView extends LinearLayout implements IProductView {

    public static final String TAG = "ProductView";

    @BindView(R.id.product_name_txt)
    TextView mProductNameTxt;
    @BindView(R.id.product_description_txt)
    TextView mProductDescriptionTxt;
    @BindView(R.id.product_image)
    ImageView mProductImage;
    @BindView(R.id.product_count_txt)
    TextView mProductCountTxt;
    @BindView(R.id.product_price_txt)
    TextView mProductPriceTxt;
    @BindView(R.id.plus_btn)
    ImageButton mPlusBtn;
    @BindView(R.id.minus_btn)
    ImageButton mMinusBtn;

    @Inject
    Picasso mPicasso;
    @Inject
    ProductScreen.ProductPresenter mPresenter;

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            DaggerService.<ProductScreen.Component>getDaggerComponent(context)
                    .inject(this);
        }
    }

    //region ==================== flow view lifecycle callbacks ===================
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mPresenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            mPresenter.dropView(this);
        }
    }
    //endregion

    //region ==================== IProductView ===================
    @Override
    public void showProductView(final ProductDto product) {
        mProductNameTxt.setText(product.getProductName());
        mProductDescriptionTxt.setText(product.getDescription());
        mProductCountTxt.setText(String.valueOf(product.getCount()));
        if (product.getCount() > 0) {
            mProductPriceTxt.setText(String.valueOf(product.getCount() * product
                    .getPrice() + ".-"));
        } else {
            mProductPriceTxt.setText(String.valueOf(product.getPrice() + ".-"));
        }

        //region ==================== TO BE FINE-TUNED ===================

//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        final int width, height;
//        if (metrics.widthPixels < metrics.heightPixels) {
//            width = (int) (metrics.widthPixels / 2f);
//            height = (int) (width / 1.78f);
//        } else {
//            height = (int) (metrics.heightPixels / 5f);
//            width = (int) (height * 1.78f);
//        }
//
//        mPicasso.load(product.getImageUrl())
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .resize(width, height)
//                .centerCrop()
//                .placeholder(R.drawable.placeholder)
//                .into(mProductImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e(TAG, "onSuccess: load from cache");
//                    }
//
//                    @Override
//                    public void onError() {
//                        mPicasso.load(product.getImageUrl())
//                                .resize(width, height)
//                                .centerCrop()
//                                .placeholder(R.drawable.placeholder)
//                                .into(mProductImage);
//                    }
//                });
        //endregion

        mPicasso.load(product.getImageUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .into(mProductImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        mPicasso.load(product.getImageUrl())
                                .fit()
                                .centerInside()
                                .placeholder(R.drawable.placeholder)
                                .into(mProductImage);
                    }
                });

//        Glide.with(getActivity())
//                .load(product.getImageUrl())
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder)
//                .fitCenter()
//                .dontAnimate()
////                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(mProductImage);
    }

    @Override
    public void updateProductCountView(ProductDto product) {
        mProductCountTxt.setText(String.valueOf(product.getCount()));
        if (product.getCount() >= 0) {
            mProductPriceTxt.setText(String.valueOf(product.getCount() * product
                    .getPrice() + ".-"));
        }
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }
    //endregion

    //region ==================== Events ===================
    @OnClick(R.id.plus_btn)
    void clickPlus() {
        mPresenter.clickOnPlus();
    }

    @OnClick(R.id.minus_btn)
    void clickMinus() {
        mPresenter.clickOnMinus();
    }
    //endregion


}
