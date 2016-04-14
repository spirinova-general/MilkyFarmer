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
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceclasses.CustomersService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.ui.adapters.MainCustomersListAdapter;
import com.milky.ui.customers.CustomerAddActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Customers;

import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {
    private List<Customers> _mCustomersList = new ArrayList<>();
    public static MainCustomersListAdapter _mAdapter;
    public static TextView mTotalCustomers;
    private DatabaseHelper _dbHelper;
    public static ListView recList;
    private CustomersService customersSettingService;

    public void onResume() {
        super.onResume();
        if (Constants.REFRESH_CUSTOMERS) {
            if (_dbHelper.isTableNotEmpty(TableNames.CUSTOMER)) {

                _mCustomersList = customersSettingService.getCustomersListByArea(Constants.selectedAreaId);
                if (Constants.selectedAreaId==-1) {
                    if (_mCustomersList.size() == 1)
                        mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customer");
                    else
                        mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customers");
                    _mAdapter = new MainCustomersListAdapter(getActivity(), 0, R.id.address, _mCustomersList);
                    recList.setAdapter(_mAdapter);
                } else {
                    if (_mCustomersList.size() == 1)
                        mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customer in " + MainActivity.selectedArea);
                    else
                        mTotalCustomers.setText(String.valueOf(_mCustomersList.size()) + " " + "Customers in " + MainActivity.selectedArea);
                    _mAdapter = new MainCustomersListAdapter(getActivity(), 0, R.id.address, _mCustomersList);
                    recList.setAdapter(_mAdapter);
                }


            } else
                mTotalCustomers.setText(getResources().getString(R.string.no_customer_added));
            _dbHelper.close();
            Constants.REFRESH_CUSTOMERS=false;
        }



    }

private View view=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view ==null) {
            view = inflater.inflate(R.layout.customers_fragment_layout, null);
            initResources(view);
        }
        return view;
    }

    private void initResources(View view) {
        recList = (ListView) view.findViewById(R.id.customersListView);
        customersSettingService = new CustomersService();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        mTotalCustomers = (TextView) view.findViewById(R.id.totalCustomers);


        FloatingActionButton mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if global setting has been set
                if (new GlobalSettingsService().getData().getDefaultRate() < 1) {
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
