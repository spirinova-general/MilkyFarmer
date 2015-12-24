package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.AreaCityTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.utils.AppUtil;
import com.milky.viewmodel.VCustomersList;

import java.util.List;


/**
 * Created by Neha on 11/18/2015.
 */
public class CustomersFragmentListAdapter extends RecyclerView.Adapter<CustomersFragmentListAdapter.CustomersViewHolder> {
    private List<VCustomersList> mCustomersList;
    private CustomersFragmentListAdapter mListAdapter;
    private Activity mActivity;
    private DatabaseHelper _dbhelper;

    public CustomersFragmentListAdapter(Activity act, List<VCustomersList> listData) {
        this.mCustomersList = listData;
        this.mActivity = act;
        _dbhelper = AppUtil.getInstance().getDatabaseHandler();
    }


    @Override
    public CustomersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.customers_fragment_items, parent, false);


        return new CustomersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomersViewHolder holder, int position) {
        VCustomersList customer = mCustomersList.get(position);
        holder.userFirstName.setText(customer.getFirstName());
        holder.userLastName.setText(customer.getLastName());
        holder.userFlatNo.setText("#" + customer.getAddress1() + ", ");
        holder.userAreaName.setText(AreaCityTableManagement.getAreaNameById(_dbhelper.getReadableDatabase(), customer.getAreaId()) + ", ");
        holder.userStreet.setText(customer.getAddress2() + ", ");
        holder.userCity.setText(AreaCityTableManagement.getCityNameById(_dbhelper.getReadableDatabase(), customer.getCityId()));

    }

    @Override
    public int getItemCount() {
        return mCustomersList.size();
    }

    class CustomersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView userFirstName, userLastName;
        protected TextView userFlatNo;
        protected TextView userAreaName;
        protected TextView userStreet;
        protected TextView userCity;

        public CustomersViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            userFirstName = (TextView) itemView.findViewById(R.id.first_name);
            userLastName = (TextView) itemView.findViewById(R.id.last_name);
            userFlatNo = (TextView) itemView.findViewById(R.id.flat_number);
            userAreaName = (TextView) itemView.findViewById(R.id.area_name);
            userStreet = (TextView) itemView.findViewById(R.id.street);
            userCity = (TextView) itemView.findViewById(R.id.city);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, com.milky.ui.main.CustomersActivity.class)
                    .putExtra("fname", mCustomersList.get(getPosition()).getFirstName())
                    .putExtra("lname", mCustomersList.get(getPosition()).getLastName())
                    .putExtra("quantity", mCustomersList.get(getPosition()).get_mQuantity())
                    .putExtra("areaId", mCustomersList.get(getPosition()).getAreaId())
                    .putExtra("address1", mCustomersList.get(getPosition()).getAddress1())
                    .putExtra("istoAddCustomer", false)
                    .putExtra("cityId", mCustomersList.get(getPosition()).getCityId())
                    .putExtra("mobile", mCustomersList.get(getPosition()).getMobile())
                    .putExtra("defaultrate", mCustomersList.get(getPosition()).getRate())
                    .putExtra("address2", mCustomersList.get(getPosition()).getAddress2())
                    .putExtra("added_date", mCustomersList.get(getPosition()).getDateAdded())
                    .putExtra("balance", mCustomersList.get(getPosition()).getBalance_amount())
                    .putExtra("cust_id", mCustomersList.get(getPosition()).getCustomerId())
                    .putExtra("delivery_date", mCustomersList.get(getPosition()).getDeliverydate());
            mActivity.startActivity(intent);

        }
    }
}

