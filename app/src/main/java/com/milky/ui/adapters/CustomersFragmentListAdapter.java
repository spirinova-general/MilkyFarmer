package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.AreaCityTableManagement;
import com.milky.service.databaseutils.AreaMapTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.ui.customers.CustomersList;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VCustomersList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Neha on 11/18/2015.
 */
public class CustomersFragmentListAdapter extends RecyclerView.Adapter<CustomersFragmentListAdapter.CustomersViewHolder> implements Filterable{
    private List<VCustomersList> mCustomersList, items,tempList,tempItems, suggestions;
    private CustomersFragmentListAdapter mListAdapter;
    private Activity mActivity;
    private DatabaseHelper _dbhelper;

    public CustomersFragmentListAdapter(Activity act, List<VCustomersList> listData) {
        this.mCustomersList = listData;
        this.mActivity = act;
        _dbhelper = AppUtil.getInstance().getDatabaseHandler();
        tempItems = new ArrayList<VCustomersList>(mCustomersList); // this makes the difference.
        suggestions = new ArrayList<>();
    }


    @Override
    public CustomersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.global_customers_list_item, parent, false);


        return new CustomersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomersViewHolder holder, int position) {
        VCustomersList customer = mCustomersList.get(position);
        holder.userFirstName.setText(customer.getFirstName());
        holder.userLastName.setText(customer.getLastName());
        holder.userFlatNo.setText(customer.getAddress1() + ", ");
        holder.userAreaName.setText(AreaCityTableManagement.getAreaNameById(_dbhelper.getReadableDatabase(), customer.getAreaId()) + ", ");
        holder.userStreet.setText(customer.getAddress2() + ", ");
        holder.userCity.setText(AreaCityTableManagement.getCityNameById(_dbhelper.getReadableDatabase(), customer.getCityId()));
        String a = Character.toString(customer.getFirstName().charAt(0));
        String b = Character.toString(customer.getLastName().charAt(0));

        //get first name and last name letters
        holder._nameView.setText(a + b);

        _dbhelper.close();

    }
    @Override
    public Filter getFilter() {
        return nameFilter;
    }
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((VAreaMapper) resultValue).getCityArea();

            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (VCustomersList Area : tempItems) {
                    if (Area.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(Area);
                    }
                }
                if(suggestions.size()>0)
                    Constants.validArea = true;
                else
                    Constants.validArea=false;
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                Constants.validArea=false;
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<VCustomersList> filterList = (ArrayList<VCustomersList>) results.values;
            if (results != null && results.count > 0) {
                filterList.clear();
                for (VCustomersList Area : filterList) {
                    filterList.add(Area);
                    notifyDataSetChanged();
                }
            }
        }
    };
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
        protected TextView _nameView;

        public CustomersViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            userFirstName = (TextView) itemView.findViewById(R.id.first_name);
            userLastName = (TextView) itemView.findViewById(R.id.last_name);
            userFlatNo = (TextView) itemView.findViewById(R.id.flat_number);
            userAreaName = (TextView) itemView.findViewById(R.id.area_name);
            userStreet = (TextView) itemView.findViewById(R.id.street);
            userCity = (TextView) itemView.findViewById(R.id.city);
            _nameView = (TextView) itemView.findViewById(R.id.nameView);
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
                    .putExtra("delivery_date", mCustomersList.get(getPosition()).getStart_date());

            mActivity.startActivity(intent);

        }
    }

    public void flushFilter(){
        tempList=new ArrayList<>();
        tempList.addAll(mCustomersList);
        notifyDataSetChanged();
    }



}

