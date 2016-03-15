package com.milky.ui.customers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.ui.adapters.CustomersListAdapter;
import com.milky.ui.main.BillingEdit;
import com.milky.utils.AppUtil;
import com.milky.viewmodel.VBill;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neha on 11/20/2015.
 */
public class CustomersBillingFragment extends Fragment {
    private CustomersListAdapter _mAdapter;
    private List<ExtcalVCustomersList> _mCustomersList;
    public static ListView _mListView;
    private FloatingActionButton _mAddBillFab;
    private DatabaseHelper _dbHelper;
    private String custId = "";
    private boolean hasPreviousBills = false;
    private TextView preivousBills;
    private boolean _hasFutureBill = false;

    private ExtcalDatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_billing_list, container, false);
        initResources(view);
        db = new ExtcalDatabaseHelper(getActivity());
        custId = getActivity().getIntent().getStringExtra("cust_id");
        payment.clear();
        generateBill(custId);
        if (payment.size() > 0) {
//            _mCustomersList = CustomerSettingTableManagement.getAllCustomersByCustomerId(_dbHelper.getReadableDatabase(), getActivity().getIntent().getStringExtra("cust_id"));
            _mListView.setAdapter(new BillingAdapter(payment, getActivity()));
        }
        if (hasPreviousBills)
            ((TextView) view.findViewById(R.id.preivousBills)).setVisibility(View.GONE);
        _dbHelper.close();
        return view;
    }

    private void initResources(View v) {
        _mListView = (ListView) v.findViewById(R.id.customersListView);
        _mAddBillFab = (FloatingActionButton) v.findViewById(R.id.addBillFab);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        preivousBills = (TextView) v.findViewById(R.id.preivousBills);
        preivousBills.setVisibility(View.VISIBLE);
        _mAddBillFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BillingEdit.class)
                        .putExtra("fname", getActivity().getIntent().getStringExtra("fname"))
                        .putExtra("lname", getActivity().getIntent().getStringExtra("lname"));
                startActivity(i);
            }
        });

    }

    public static ArrayList<VBill> payment = new ArrayList<>();
    //Calculating total qty

private void generateBill(String custId) {

    BillTableManagement.getHistoryBills(_dbHelper.getReadableDatabase(), custId);
    BillTableManagement.getTotalBillById(_dbHelper.getReadableDatabase(),custId);


}

}