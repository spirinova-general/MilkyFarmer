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
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.ui.adapters.CustomersListAdapter;
import com.milky.ui.main.BillingEdit;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomersList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.milky.R;

/**
 * Created by Neha on 11/20/2015.
 */
public class CustomersBillingFragment extends Fragment {
    private CustomersListAdapter _mAdapter;
    private List<VCustomersList> _mCustomersList;
    public static ListView _mListView;
    private FloatingActionButton _mAddBillFab;
    private DatabaseHelper _dbHelper;
    private String custId = "";
    private ArrayList<String> names = new ArrayList<>();
    private boolean hasPreviousBills = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_billing_list, container, false);
        initResources(view);
        custId = getActivity().getIntent().getStringExtra("cust_id");
        generateBill();
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
    double payMade = 0;

    private void generateBill() {
        payment.clear();
        names.clear();
        Calendar cal = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        double totalQuantity = 0, totalRate = 0;
        VBill holder = new VBill();

        ArrayList<VBill> bills = BillTableManagement.getOutstandingsBill(_dbHelper.getReadableDatabase());
        if(bills.size()>0)
            hasPreviousBills = true;
        for (int x = 0; x < bills.size(); x++) {
            payment.add(bills.get(x));
//            String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
//            String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
            names.add(custId);
        }

        ArrayList<String> startDates = CustomerSettingTableManagement.getStartDeliveryDate(_dbHelper.getReadableDatabase(), custId);
        if (startDates != null)
            for (int j = 0; j < startDates.size(); j++) {

                try {
                    Date date = Constants.work_format.parse(startDates.get(j));
                    c.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int i = c.get(Calendar.DAY_OF_MONTH); i <= cal.get(Calendar.DAY_OF_MONTH); ++i) {
                    double quantity = getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" +
                            String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    totalQuantity += quantity;

                    holder.setStartDate(startDates.get(j));
                    holder.setBalance(CustomersTableMagagement.getBalanceForCustomer(_dbHelper.getReadableDatabase(), custId));
                    holder.setEndDate(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    totalRate = BillTableManagement.getTotalRate(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    holder.setRate(String.valueOf(totalRate));
                    holder.setQuantity(String.valueOf(totalQuantity));

                }

            }
        payMade = BillTableManagement.getPreviousBill(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQuantity);

        holder.setPaymentMode(String.valueOf(round(payMade, 2)));
        BillTableManagement.updateTotalQuantity(_dbHelper.getWritableDatabase(), holder.getQuantity(), custId);
        if (BillTableManagement.isToBeOutstanding(_dbHelper.getReadableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1))) {
            BillTableManagement.updateOutstandingBill(_dbHelper.getWritableDatabase(), custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1));
//            holder.setStartDate(String.work_format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.work_format("%02d", 1) + "-" + String.work_format("%02d", cal.get(Calendar.YEAR)));
//            holder.setEndDate(String.work_format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.work_format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + "-" + String.work_format("%02d", cal.get(Calendar.YEAR)));
//            BillTableManagement.insertNewBills(_dbHelper.getWritableDatabase(), holder);
//            CustomerSettingTableManagement.insertNewCustomersSetting(_dbHelper.getWritableDatabase(),holder);

        }
        payment.add(holder);
//        String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
//        String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
        names.add(custId);
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private String d = "";

    public double getQtyOfCustomer(String day) {
        double qty = 0;
        double adjustedQty = 0;
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            if (DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_dbHelper.getReadableDatabase(), day, custId) == 0) {
                if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {

                    qty = CustomerSettingTableManagement.getAllCustomersByCustId(_dbHelper.getReadableDatabase(), day
                            , custId);

                }
            } else
                qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_dbHelper.getReadableDatabase(), day, custId);


        } else if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {

            qty = CustomerSettingTableManagement.getAllCustomersByCustId(_dbHelper.getReadableDatabase(), day
                    , custId);

        }


        return qty;
    }

    private String totalBill(double price, double qty) {
//        if(Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)))>0)
//            amount = ((Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))))
//                    / 100) * (float)quantity;
        double tax = (price * Double.parseDouble(Account.getDefautTax(_dbHelper.getReadableDatabase())));

        return String.valueOf(round((price * qty) + tax, 2));


    }

}