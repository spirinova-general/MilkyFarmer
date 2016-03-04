package com.milky.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.MainCustomersListAdapter;
import com.milky.ui.customers.CustomerAddActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

import java.util.List;

/**
 * Created by Neha on 11/17/2015.
 */
public class CustomersFragment extends Fragment {

    private List<ExtcalVCustomersList> _mCustomersList;
    public static MainCustomersListAdapter _mAdapter;
    private FloatingActionButton mFab;
    public static TextView mTotalCustomers;
    private DatabaseHelper _dbHelper;
    public static ListView recList;


    @Override
    public void onResume() {
        super.onResume();
        if (Constants.REFRESH_CALANDER) {
            if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {

                ((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (Constants.selectedAreaId.equals("")) {
                            _mCustomersList = CustomersTableMagagement.getAllCustomers(_dbHelper.getReadableDatabase());
                            if (_mCustomersList.size() == 1)
                                mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customer");
                            else
                                mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customers");
                            _mAdapter = new MainCustomersListAdapter(getActivity(), 0, R.id.address, _mCustomersList);
                            recList.setAdapter(_mAdapter);
                        } else {
                            _mCustomersList = CustomersTableMagagement.getAllCustomersByArea(_dbHelper.getReadableDatabase(), Constants.selectedAreaId);
                            if (_mCustomersList.size() == 1)
                                mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customer in " + MainActivity.selectedArea);
                            else
                                mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customers in " + MainActivity.selectedArea);
                            _mAdapter = new MainCustomersListAdapter(getActivity(), 0, R.id.address, _mCustomersList);
                            recList.setAdapter(_mAdapter);
                        }

                    }
                });

                _mCustomersList = CustomersTableMagagement.getAllCustomers(_dbHelper.getReadableDatabase());
                if (_mCustomersList.size() == 1)
                    mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customer");
                else
                    mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customers ");
                _mAdapter = new MainCustomersListAdapter(getActivity(), 0, R.id.address, _mCustomersList);
                recList.setAdapter(_mAdapter);


            } else
                mTotalCustomers.setText(getResources().getString(R.string.no_customer_added));
            _dbHelper.close();

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customers_fragment_layout, null);
        initResources(view);
        Constants.REFRESH_CALANDER = true;
        return view;
    }

    private void initResources(View view) {
        recList = (ListView) view.findViewById(R.id.customersListView);

        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        mTotalCustomers = (TextView) view.findViewById(R.id.totalCustomers);


        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if global setting has been set
                if ("0".equals(Account.getDefaultRate(_dbHelper.getReadableDatabase()))) {
                    MainActivity.mDrawerLayout.openDrawer(MainActivity.mNavigationView);
                    Toast.makeText(getActivity(), getResources().getString(R.string.set_global_rate), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), CustomerAddActivity.class).putExtra("istoAddCustomer", true);
                    startActivity(intent);
                }
                _dbHelper.close();
            }
        });
        mTotalCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).SyncNow();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }


}
