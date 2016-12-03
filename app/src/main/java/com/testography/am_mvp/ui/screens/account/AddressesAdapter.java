package com.testography.am_mvp.ui.screens.account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.testography.am_mvp.R;
import com.testography.am_mvp.data.storage.dto.UserAddressDto;

import java.util.ArrayList;

public class AddressesAdapter extends RecyclerView
        .Adapter<AddressesAdapter.AddressViewHolder> implements SwipeListener {

    private ArrayList<UserAddressDto> mUserAddresses;

    public AddressesAdapter(ArrayList<UserAddressDto> userAddressesDto) {
        mUserAddresses = userAddressesDto;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recycler_view_address, parent, false);

        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        UserAddressDto info = mUserAddresses.get(position);
        holder.name.setText(info.getName());
        StringBuilder addressBuilder = buildAddress(info);
        holder.completeAddress.setText(addressBuilder);
        holder.comment.setText(info.getComment());
    }

    private StringBuilder buildAddress(UserAddressDto info) {
        StringBuilder addressBuilder = new StringBuilder();

        addressBuilder.append(info.getStreet());
        addressBuilder.append(" ");
        addressBuilder.append(info.getHouse());
        addressBuilder.append(" - ");
        addressBuilder.append(info.getApartment());
        addressBuilder.append(", ");
        addressBuilder.append(info.getFloor());
        addressBuilder.append(" floor");

        return addressBuilder;
    }

    @Override
    public int getItemCount() {
        return mUserAddresses.size();
    }

    @Override
    public void onSwipe(int position) {
        mUserAddresses.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private EditText completeAddress;
        private EditText comment;

        public AddressViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.account_address_name_txt);
            completeAddress = (EditText) itemView.findViewById(R.id
                    .account_address_street_et);
            comment = (EditText) itemView.findViewById(R.id.account_address_comment_et);

        }
    }
}
