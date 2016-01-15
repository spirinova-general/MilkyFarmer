package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.AreaCityTableManagement;
import com.milky.service.databaseutils.AreaMapTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.ui.main.CustomersFragment;
import com.milky.ui.main.MainActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VCustomersList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Neha on 11/18/2015.
 */
public class MainCustomersListAdapter extends ArrayAdapter<VCustomersList> {
    private List<VCustomersList> mCustomersList, tempItems, suggestions;
    private CustomersFragmentListAdapter mListAdapter;
    private Activity mActivity;
    private DatabaseHelper _dbhelper;

    public MainCustomersListAdapter(Activity act, int resource, int textViewResourceId, List<VCustomersList> listData) {
        super(act, resource, textViewResourceId, listData);
        this.mCustomersList = listData;
        this.mActivity = act;
        _dbhelper = AppUtil.getInstance().getDatabaseHandler();
        tempItems = new ArrayList<VCustomersList>(mCustomersList); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mCustomersList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.global_customers_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.userFirstName = (TextView) convertView.findViewById(R.id.first_name);
        holder.userLastName = (TextView) convertView.findViewById(R.id.last_name);
        holder.userFlatNo = (TextView) convertView.findViewById(R.id.flat_number);
        holder.userAreaName = (TextView) convertView.findViewById(R.id.area_name);
        holder.userStreet = (TextView) convertView.findViewById(R.id.street);
        holder.userCity = (TextView) convertView.findViewById(R.id.city);
        holder._nameView = (TextView) convertView.findViewById(R.id.nameView);


        final VCustomersList customer = mCustomersList.get(position);
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(Constants.work_format.parse(customer.getStart_date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String deliveryDate = c.get(Calendar.DAY_OF_MONTH) + "-" + Constants.MONTHS[c.get(Calendar.MONTH)] + "-" + c.get(Calendar.YEAR);
                Intent intent = new Intent(mActivity, com.milky.ui.main.CustomersActivity.class)
                        .putExtra("fname", customer.getFirstName())
                        .putExtra("lname", customer.getLastName())
                        .putExtra("quantity", customer.get_mQuantity())
                        .putExtra("areaId", customer.getAreaId())
                        .putExtra("address1", customer.getAddress1())
                        .putExtra("istoAddCustomer", false)
                        .putExtra("cityId", customer.getCityId())
                        .putExtra("mobile", customer.getMobile())
                        .putExtra("defaultrate", customer.getRate())
                        .putExtra("address2", customer.getAddress2())
                        .putExtra("added_date", customer.getDateAdded())
                        .putExtra("balance", customer.getBalance_amount())
                        .putExtra("cust_id", customer.getCustomerId())
                        .putExtra("delivery_date", deliveryDate)
                        .putExtra("start_delivery_date", customer.getStart_date());
                mActivity.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((VCustomersList) resultValue).getFirstName();
            notifyDataSetChanged();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (VCustomersList Area : tempItems) {
                    if ((Area.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            Area.getLastName().toLowerCase().contains(constraint.toString().toLowerCase()))) {
                        suggestions.add(Area);
                    }
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<VCustomersList> filterList = (ArrayList<VCustomersList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (VCustomersList Area : filterList) {
                    add(Area);
                    notifyDataSetChanged();
                }
                if (results.count == 1)
                    CustomersFragment.mTotalCustomers.setText(results.count + " Customer");
                else if (results.count > 1)
                    CustomersFragment.mTotalCustomers.setText(results.count + " Customers");
            } else {
                clear();
                notifyDataSetChanged();
            }
            if (!Constants.selectedAreaId.equals("")) {
                clear();

                VAreaMapper holder = AreaMapTableManagement.getAreabyAreaId(_dbhelper.getReadableDatabase(), Constants.selectedAreaId);

                filterList = CustomersTableMagagement.getAllCustomersByArea(_dbhelper.getReadableDatabase(), Constants.selectedAreaId);
                for (VCustomersList Area : filterList) {
                    add(Area);
                    notifyDataSetChanged();
                }
                if (filterList.size() == 1) {

                    CustomersFragment.mTotalCustomers.setText(filterList.size() + " Customer in " + holder.getArea()
                            + ", " + AreaMapTableManagement.getCityNameById(_dbhelper.getReadableDatabase(), Constants.selectedCityId));

                } else if (filterList.size() > 1)
                    CustomersFragment.mTotalCustomers.setText(filterList.size() + " Customers");
            }
            notifyDataSetChanged();


        }
    };

    class ViewHolder {
        protected TextView userFirstName, userLastName;
        protected TextView userFlatNo;
        protected TextView userAreaName;
        protected TextView userStreet;
        protected TextView userCity;
        protected TextView _nameView;
    }


}