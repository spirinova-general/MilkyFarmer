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
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.ui.adapters.CustomersListAdapter;
import com.milky.ui.main.BillingEdit;
import com.milky.utils.AppUtil;
import com.milky.service.core.Bill;
import com.milky.service.core.Customers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neha on 11/20/2015.
 */
public class CustomersBillingFragment extends Fragment {
    private CustomersListAdapter _mAdapter;
    private List<Customers> _mCustomersList;
    public static ListView _mListView;
    private FloatingActionButton _mAddBillFab;
    private DatabaseHelper _dbHelper;
    private int custId = 0;
    private boolean hasPreviousBills = false;
    private TextView preivousBills;
    private boolean _hasFutureBill = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.customer_billing_list, container, false);

        initResources(view);
        //Umesh check if we need this in both OnCrateView and setUserVisibleHint
        IBill billService = new BillService();
        List<Bill> bills = billService.getBillsOfCustomer(custId);

        if (bills.size() > 0)
            ((TextView) view.findViewById(R.id.preivousBills)).setVisibility(View.GONE);
        return view;
    }

    private View view = null;

    private void initResources(View v) {
        _mListView = (ListView) v.findViewById(R.id.customersListView);
        _mAddBillFab = (FloatingActionButton) v.findViewById(R.id.addBillFab);
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

    //Calculating total qty

    /*private void generateBill() {
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        BillService billService = new BillService();
        //billService.getOutstandingBillsById(custId);
        //billService.getTotalBillById(custId);
        List<Bill> bills = billService.getBillsOfCustomer(custId);
        if (bills.size() > 0) {
            BillingAdapter  adapter = new BillingAdapter(bills, getActivity());
            _mListView.setAdapter(adapter);

        }
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            custId = getActivity().getIntent().getIntExtra("cust_id",0);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            if (view == null)
                view = inflater.inflate(R.layout.customer_billing_list, null, false);
            _mListView = (ListView) view.findViewById(R.id.customersListView);

            IBill billService = new BillService();
            List<Bill> bills = billService.getBillsOfCustomer(custId);
            if (bills.size() > 0) {
                BillingAdapter  adapter = new BillingAdapter(bills, getActivity());
                _mListView.setAdapter(adapter);

            }

            if (bills.size() > 0) {
                _mListView.setAdapter(new BillingAdapter(bills, getActivity()));
            }
            _dbHelper.close();
        }
    }

}