package com.testography.am_mvp.ui.screens.account;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.testography.am_mvp.R;
import com.testography.am_mvp.data.storage.dto.UserAddressDto;
import com.testography.am_mvp.data.storage.dto.UserDto;
import com.testography.am_mvp.di.DaggerService;
import com.testography.am_mvp.mvp.views.IAccountView;
import com.testography.am_mvp.ui.activities.RootActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;

public class AccountView extends CoordinatorLayout implements IAccountView,
        RootActivity.AvatarHolderCallback {

    public static final int PREVIEW_STATE = 1;
    public static final int EDIT_STATE = 0;

    @Inject
    AccountScreen.AccountPresenter mPresenter;
    @Inject
    Picasso mPicasso;

    @BindView(R.id.profile_name_txt)
    TextView profileNameTxt;
    @BindView(R.id.user_avatar_img)
    ImageView mUserAvatarImg;
    @BindView(R.id.user_phone_et)
    EditText userPhoneEt;
    @BindView(R.id.user_full_name_et)
    EditText userFullNameEt;
    @BindView(R.id.profile_name_wrapper)
    LinearLayout profileNameWrapper;
    @BindView(R.id.address_list)
    RecyclerView mAddressList;
    @BindView(R.id.add_address_btn)
    Button addAddressBtn;
    @BindView(R.id.notification_order_sw)
    SwitchCompat notificationOrderSw;
    @BindView(R.id.notification_promo_sw)
    SwitchCompat notificationPromoSw;

    private AccountScreen mScreen;
    private UserDto mUserDto;
    private TextWatcher mWatcher;
    private AddressesAdapter mAddressesAdapter;

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            mScreen = Flow.getKey(this);
            DaggerService.<AccountScreen.Component>getDaggerComponent(context).inject
                    (this);
        }
    }

    //region ==================== flow view lifecycle callbacks ===================
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    private void showViewFromState() {
        if (mScreen.getCustomState() == PREVIEW_STATE) {
            showPreviewState();
        } else {
            showEditState();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mPresenter.takeView(this);
            if (mPresenter.getRootView() != null) {
                mPresenter.getRootView().setAvatarCallback(this);
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            mPresenter.dropView(this);
        }
    }
    //endregion

    public void initView(UserDto user) {
        mUserDto = user;
        initProfileInfo();
        initList();
        initSettings();
        showViewFromState();
    }

    private void initSettings() {
        notificationOrderSw.setChecked(mUserDto.isOrderNotification());
        notificationOrderSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.switchOrder(b);
            }
        });

        notificationPromoSw.setChecked(mUserDto.isPromoNotification());
        notificationPromoSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.switchPromo(b);
            }
        });
    }

    private void initList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAddressList.setLayoutManager(layoutManager);
        mAddressList.setVisibility(VISIBLE);

        ArrayList<UserAddressDto> userAddresses = mUserDto.getUserAddresses();
        mAddressesAdapter = new AddressesAdapter(userAddresses);
        mAddressList.setAdapter(mAddressesAdapter);

        SimpleTouchCallback callback = new SimpleTouchCallback(mAddressesAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mAddressList);
    }

    private void initProfileInfo() {
        profileNameTxt.setText(mUserDto.getFullname());
        userFullNameEt.setText(mUserDto.getFullname());
        userPhoneEt.setText(mUserDto.getPhone());
        mPicasso.load(mUserDto.getAvatar())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(mUserAvatarImg);
    }

    //region ==================== IAccountView ===================
    @Override
    public void changeState() {
        if (mScreen.getCustomState() == PREVIEW_STATE) {
            mScreen.setCustomState(EDIT_STATE);
        } else {
            mScreen.setCustomState(PREVIEW_STATE);
        }
        showViewFromState();
    }

    @Override
    public void showEditState() {
        mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileNameTxt.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        profileNameWrapper.setVisibility(VISIBLE);
        userFullNameEt.addTextChangedListener(mWatcher);
        userPhoneEt.setEnabled(true);
        mPicasso.load(R.drawable.ic_add_white_24dp)
                .error(R.drawable.ic_add_white_24dp)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(mUserAvatarImg);
    }

    @Override
    public void showPreviewState() {
        profileNameWrapper.setVisibility(GONE);
        userPhoneEt.setEnabled(false);
        userFullNameEt.removeTextChangedListener(mWatcher);
        mPicasso.load(mUserDto.getAvatar())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(mUserAvatarImg);
    }

    @Override
    public void showPhotoSourceDialog() {
        String source[] = {getContext().getString(R.string.choose_photo), getContext().getString(R.string.take_photo)};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setTitle(R.string.change_photo);
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setItems(source, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        mPresenter.chooseGallery();
                        break;
                    case 1:
                        mPresenter.chooseCamera();
                        break;
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public String getUserName() {
        return String.valueOf(userFullNameEt.getText());
    }

    @Override
    public String getUserPhone() {
        return String.valueOf(userPhoneEt.getText());
    }

    @Override
    public boolean viewOnBackPressed() {
        if (mScreen.getCustomState() == EDIT_STATE) {
            changeState();
            return true;
        } else {
            return false;
        }
    }
    //endregion

    //region ==================== Events ===================

    @OnClick(R.id.collapsing_toolbar)
    void testEditMode() {
        mPresenter.switchViewState();
    }

    @OnClick(R.id.add_address_btn)
    void clickAddAddress() {
        mPresenter.onClickAddress();
    }

    @OnClick(R.id.user_avatar_img)
    void clickUserAvatar() {
        if (mScreen.getCustomState() == EDIT_STATE) {
            mPresenter.takePhoto();
        }
    }

    @Override
    public void passSelectedImage(Uri avatar) {
        mPresenter.setAvatar(avatar);
    }

    //endregion
}
