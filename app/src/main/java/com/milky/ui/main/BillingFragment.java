package com.milky.ui.main;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillingFragment extends Fragment {
    public static ListView _mListView;
    private DatabaseHelper _dbHelper;
    int custId = 0;
    IBill billService = new BillService();

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                if (_dbHelper.isTableNotEmpty(TableNames.Bill)) {
                    if (Constants.REFRESH_BILL) {
                        new UpdataBills(true).execute();
                    }
                } else {
                    Toast.makeText(getActivity(), "No customer is added yet..", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_customers_list, container, false);
            initResources(view);
        }
        return view;

    }

    private BillingAdapter adapter;
    List<Bill> bills;
    private class UpdataBills extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        private boolean _reCalculateBills = false;
        public UpdataBills(boolean reCalculate)
        {
            _reCalculateBills = reCalculate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Refreshing...", false, true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            //billService.RecalculateAllCurrentBills();
            bills = billService.getAllGlobalBills(_reCalculateBills);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bills.size() > 0) {
                adapter = new BillingAdapter(bills, getActivity());
                _mListView.setAdapter(adapter);
            }
            progressDialog.dismiss();
            Constants.REFRESH_BILL=false;

            if( _reCalculateBills)
                Toast.makeText(getActivity(), "All Bills Refreshed...", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Bills Refreshed...", Toast.LENGTH_SHORT).show();
        }
    }

    //protected CustomersSettingService settingData;

    private void initResources(View v) {
        _mListView = (ListView) v.findViewById(R.id.customersList);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        custId = getActivity().getIntent().getIntExtra("cust_id", 0);
        //settingData = new CustomersSettingService().getByCustId(custId, Constants.getCurrentDate());

        boolean hasPreviousBills = false;
        if (hasPreviousBills)
            ((TextView) v.findViewById(R.id.preivousBills)).setVisibility(View.GONE);
        _dbHelper.close();
    }

    public static ArrayList<Bill> payment = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (Constants.REFRESH_BILL) {
//                progressDialog = ProgressDialog.show(getActivity(), "", "Refreshing...", false, true);
                new UpdataBills(false).execute();
            }

        }
    }


}
