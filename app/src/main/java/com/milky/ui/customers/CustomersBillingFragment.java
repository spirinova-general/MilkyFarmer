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

import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.ui.adapters.CustomersListAdapter;
import com.milky.ui.main.BillingEdit;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.milky.R;
import com.tyczj.extendedcalendarview.DeliveryTableManagement;
import com.tyczj.extendedcalendarview.ExtcalCustomerSettingTableManagement;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

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
    private ArrayList<String> names = new ArrayList<>();
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
        names.clear();
        generateBill(custId);
        if (payment.size() > 0) {
//            _mCustomersList = CustomerSettingTableManagement.getAllCustomersByCustomerId(_dbHelper.getReadableDatabase(), getActivity().getIntent().getStringExtra("cust_id"));
            _mListView.setAdapter(new BillingAdapter(payment, getActivity(), names));
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

    ArrayList<VBill> payment = new ArrayList<>();
    //Calculating total qty

private void generateBill(String custId) {
     ArrayList<VBill> bills = BillTableManagement.getHistoryBills(_dbHelper.getReadableDatabase(), custId);
    if (bills.size() > 0)
        hasPreviousBills = true;
    for (int x = 0; x < bills.size(); x++) {
        payment.add(bills.get(x));
//            String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
//            String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
        names.add(custId);

    }
    String startDate = BillTableManagement.getStartDatebyCustomerId(_dbHelper.getReadableDatabase(), custId);

    if(!"".equals(startDate)) {
        VBill holder = BillTableManagement.getTotalBill(_dbHelper.getReadableDatabase(), custId, startDate);
        if(holder !=null) {
            payment.add(holder);
            names.add(custId);
            BillTableManagement.updateTotalQuantity(_dbHelper.getWritableDatabase(), holder.getQuantity(), holder.getBillMade(), custId);
        }
    }


}

}