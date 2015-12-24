package com.milky.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.CustomersListAdapter;
import com.milky.utils.AppUtil;
import com.milky.viewmodel.VCustomersList;

import java.util.List;

import com.milky.R;


/**
 * Created by Neha on 11/18/2015.
 */
public class BillingFragment extends Fragment {

    private CustomersListAdapter _mAdapter;
    private List<VCustomersList> _mCustomersList;
    public static ListView _mListView;
    private DatabaseHelper _dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_customers_list, container, false);
        initResources(view);
        return view;

    }

    private void initResources(View v) {
        _mListView = (ListView) v.findViewById(R.id.customersList);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        TextView _mTotalBills = (TextView) v.findViewById(R.id.total_pending_bills);
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            //  _mCustomersList = CustomersTableMagagement.getAllCustomers(_dbHelper.getReadableDatabase());
//            _mAdapter = new CustomersListAdapter(_mCustomersList, getActivity(), false);
//            _mListView.setAdapter(_mAdapter);
        }

//        _mTotalBills.setVisibility(View.VISIBLE);


    }

    public void onClick(View v) {

    }
}
