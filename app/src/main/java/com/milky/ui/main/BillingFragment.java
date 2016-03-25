package com.milky.ui.main;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomers;

import java.util.ArrayList;


/**
 * Created by Neha on 11/18/2015.
 */
public class BillingFragment extends Fragment {
    public static ListView _mListView;
    private DatabaseHelper _dbHelper;
    private ArrayList<VCustomers> list;
    private boolean hasPreviousBills = false;

    @Override
    public void onResume() {
        super.onResume();

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

    private class UpdataBills extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    payment.clear();
                    if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
                        BillTableManagement.getOutstandingsBill(_dbHelper.getReadableDatabase());
                        BillTableManagement.getTotalBill(_dbHelper.getReadableDatabase());
                    }
                    if (payment.size() > 0) {
                        adapter = new BillingAdapter(payment, getActivity());
                        _mListView.setAdapter(adapter);

                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    private void initResources(View v) {
        _mListView = (ListView) v.findViewById(R.id.customersList);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        TextView _mTotalBills = (TextView) v.findViewById(R.id.total_pending_bills);

        if (hasPreviousBills)
            ((TextView) v.findViewById(R.id.preivousBills)).setVisibility(View.GONE);
        _dbHelper.close();
    }

     public static ArrayList<VBill> payment = new ArrayList<>();
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (Constants.REFRESH_BILL) {
                new UpdataBills().execute();
            }
            Constants.REFRESH_BILL = false;
        }
    }

}
