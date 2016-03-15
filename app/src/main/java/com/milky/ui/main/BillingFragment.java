package com.milky.ui.main;


import android.app.ProgressDialog;
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
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

import java.util.ArrayList;


/**
 * Created by Neha on 11/18/2015.
 */
public class BillingFragment extends Fragment {

    public static ListView _mListView;
    private DatabaseHelper _dbHelper;
    private ArrayList<ExtcalVCustomersList> list;
    private boolean hasPreviousBills = false;
    private ExtcalDatabaseHelper _exDb;
    public static String addedCustomerId = "";

    @Override
    public void onResume() {
        super.onResume();
        if(Constants.REFRESH_BILL) {
            new UpdataBills().execute();
        }
        else
        if (payment.size() > 0) {
            adapter = new BillingAdapter(payment, getActivity());
            _mListView.setAdapter(adapter);

        }
        Constants.REFRESH_BILL=false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    private  View view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view ==null) {
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
        _exDb = new ExtcalDatabaseHelper(getActivity());
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        TextView _mTotalBills = (TextView) v.findViewById(R.id.total_pending_bills);

        if (hasPreviousBills)
            ((TextView) v.findViewById(R.id.preivousBills)).setVisibility(View.GONE);
        _dbHelper.close();
    }

    public void onClick(View v) {

    }

    public static ArrayList<VBill> payment = new ArrayList<>();
    //Calculating total qty
    double payMade = 0;

//    private void generateBill(String custId) {
//        Calendar cal = Calendar.getInstance();
//        Calendar c = Calendar.getInstance();
//        double totalQuantity = 0, totalRate = 0;
//        String startDeliveryDate = "";
//        VBill holder = new VBill();
//        /*Bill is to be outstanding*/
////        if (cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
////            // update outstanding bills
////            BillTableManagement.updateOutstandingBills(_dbHelper.getWritableDatabase(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
////            VCustomersList custHolder = CustomersTableMagagement.getAllCustomersByCustId(_dbHelper.getReadableDatabase(), custId);
////            Calendar nextMonth = Calendar.getInstance();
////            nextMonth.add(Calendar.MONTH, 1);
////            custHolder.setStart_date(cal.get(Calendar.YEAR) + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH) + 1) + "-" +
////                    "01");
////
////            custHolder.setEnd_date(cal.get(Calendar.YEAR) + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH) + 1) + "-" +
////                    String.format("%02d", nextMonth.getActualMaximum(Calendar.DAY_OF_MONTH)));
////           //Insert new bill and setting for customer
////            CustomerSettingTableManagement.insertCustomersSetting(_dbHelper.getWritableDatabase(), custHolder);
////            custHolder.setTax(Account.getDefautTax(_dbHelper.getReadableDatabase()));
////            custHolder.setAdjustment("");
////            custHolder.setPaymentMade("0");
////            custHolder.setIsCleared("1");
////            custHolder.setDateModified(custHolder.getStart_date());
////            BillTableManagement.insertBillData(_dbHelper.getWritableDatabase(), custHolder);
////
////
////        }
//
////        if (BillTableManagement.isToBeOutstanding(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1))) {
////            BillTableManagement.updateOutstandingBill(_dbHelper.getWritableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1));
//////            holder.setStartDate(String.work_format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.work_format("%02d", 1) + "-" + String.work_format("%02d", cal.get(Calendar.YEAR)));
//////            holder.setEndDate(String.work_format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.work_format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + "-" + String.work_format("%02d", cal.get(Calendar.YEAR)));
////            BillTableManagement.insertNewBills(_dbHelper.getWritableDatabase(), holder);
//////            CustomerSettingTableManagement.insertNewCustomersSetting(_dbHelper.getWritableDatabase(), holder);
////
////        }
//        ArrayList<VBill> bills = BillTableManagement.getOutstandingsBill(_dbHelper.getReadableDatabase(),custId);
//        if (bills.size() > 0)
//            hasPreviousBills = true;
//        for (int x = 0; x < bills.size(); x++) {
//            payment.add(bills.get(x));
////            String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
////            String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
//            custIdsList.add(custId);
//
//        }
////TODO ExtCal SETTINGS DB
//        ArrayList<String> startDates = ExtcalCustomerSettingTableManagement.getStartDeliveryDate(_exDb.getReadableDatabase(), custId);
//        if (startDates != null)
//            for (int j = 0; j < startDates.size(); j++) {
//
//                try {
//                    Date date = Constants.work_format.parse(startDates.get(j));
//                    c.setTime(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (totalQuantity > 0 && startDates.get(j).equals(startDeliveryDate)) {
//                    return;
//                }
//
//                for (int i = c.get(Calendar.MONTH);i<=cal.get(Calendar.MONTH); ++i) {
//                    if (BillTableManagement.isClearedBill(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH)+1) + "-" + String.format("%02d", i))) {
//
//                        double quantity = getQtyOfCustomer(
//                                cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
//                                        + String.format("%02d", i), custId);
//                        totalQuantity += quantity;
//                        holder.setCustomerId(custId);
//                        startDeliveryDate = startDates.get(j);
//                        holder.setStartDate(startDates.get(j));
//                        holder.setBalance(CustomersTableMagagement.getBalanceForCustomer(_dbHelper.getReadableDatabase(), custId));
//                        holder.setEndDate(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
//                        totalRate = BillTableManagement.getTotalRate(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
//                        holder.setRate(String.valueOf(totalRate));
//                        holder.setBalanceType(CustomersTableMagagement.getBalanceType(_dbHelper.getReadableDatabase(), custId));
//                        holder.setQuantity(String.valueOf(totalQuantity));
//                        holder.setPaymentMode(BillTableManagement.getPaymentMade(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i)));
//                        holder.setIsOutstanding(BillTableManagement.outstandingStatus(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i)));
//                        holder.setIsCleared("1");
//                        _hasFutureBill = true;
//                    }
//
//                }
//            }
//        if (_hasFutureBill) {
//            if (holder.getBalanceType().equals("1"))
//                payMade = BillTableManagement.getPreviousBill(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQuantity)
//                        + Float.parseFloat(holder.getBalance());
//            else
//                payMade = BillTableManagement.getPreviousBill(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQuantity)
//                        - Float.parseFloat(holder.getBalance());
//
//
//            holder.setBillMade(String.valueOf(round(payMade, 2)));
//            BillTableManagement.updateTotalQuantity(_dbHelper.getWritableDatabase(), holder.getQuantity(), holder.getBillMade(), custId);
//
////        String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
////        String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
//            custIdsList.add(custId);
//            payment.add(holder);
//        }
//        _hasFutureBill = false;
//    }


//    private void generateBill() {
//
//            VBill holder = BillTableManagement.getTotalBill(_dbHelper.getReadableDatabase());
//            if (holder != null) {
//                payment.add(holder);
//
//        }
//    }
}
