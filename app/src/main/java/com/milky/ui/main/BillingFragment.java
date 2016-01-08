package com.milky.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.ui.adapters.CustomersListAdapter;
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
 * Created by Neha on 11/18/2015.
 */
public class BillingFragment extends Fragment {

    private CustomersListAdapter _mAdapter;
    private List<VCustomersList> _mCustomersList;
    public static ListView _mListView;
    private DatabaseHelper _dbHelper;
    private ArrayList names = new ArrayList();

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
        payment.clear();
        names.clear();
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            ArrayList<String> list = CustomersTableMagagement.getAllCustomersIds(_dbHelper.getReadableDatabase());
            for (int i = 0; i < list.size(); ++i) {
                generateBill(list.get(i));
            }

        }
        if (payment.size() > 0) {
//            _mCustomersList = CustomerSettingTableManagement.getAllCustomersByCustomerId(_dbHelper.getReadableDatabase(), getActivity().getIntent().getStringExtra("cust_id"));
            _mListView.setAdapter(new BillingAdapter(payment, getActivity(), names));
        }
        _dbHelper.close();
    }

    public void onClick(View v) {

    }

    ArrayList<VBill> payment = new ArrayList<>();
    //Calculating total qty
    double payMade = 0;

    private void generateBill(String custId) {

        Calendar cal = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        double totalQuantity = 0, totalRate = 0;
        VBill holder = new VBill();

        ArrayList<VBill> bills = BillTableManagement.getOutstandingsBill(_dbHelper.getReadableDatabase());
        for (int x = 0; x < bills.size(); x++) {
            payment.add(bills.get(x));
            String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
            String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
            names.add(a + b);
        }

        ArrayList<String> startDates = CustomerSettingTableManagement.getStartDeliveryDate(_dbHelper.getReadableDatabase(), custId);
        if (startDates != null)
            for (int j = 0; j < startDates.size(); j++) {

                try {
                    Date date = Constants.format.parse(startDates.get(j));
                    c.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int i = c.get(Calendar.DAY_OF_MONTH); i <= cal.get(Calendar.DAY_OF_MONTH); ++i) {
                    double quantity = getQtyOfCustomer(
                            String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" + String.format("%02d", cal.get(Calendar.YEAR)), custId);
                    totalQuantity += quantity;

                    holder.setStartDate(startDates.get(j));
                    holder.setBalance(CustomersTableMagagement.getBalanceForCustomer(_dbHelper.getReadableDatabase(), custId));
                    holder.setEndDate(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
                    totalRate = BillTableManagement.getTotalRate(_dbHelper.getReadableDatabase(), custId, String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
                    holder.setRate(String.valueOf(totalRate));
                    holder.setQuantity(String.valueOf(totalQuantity));


                }

            }
        payMade = BillTableManagement.getPreviousBill(_dbHelper.getReadableDatabase(), custId, String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", cal.get(Calendar.YEAR)), totalQuantity);

        holder.setPaymentMode(String.valueOf(round(payMade, 2)));
        BillTableManagement.updateTotalQuantity(_dbHelper.getWritableDatabase(), holder.getQuantity(), custId);
        if (BillTableManagement.isToBeOutstanding(_dbHelper.getReadableDatabase(), custId, String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.YEAR)))) {
            BillTableManagement.updateOutstandingBill(_dbHelper.getWritableDatabase(), custId, String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
//            holder.setStartDate(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", 1) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
//            holder.setEndDate(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
//            BillTableManagement.insertNewBills(_dbHelper.getWritableDatabase(), holder);
//            CustomerSettingTableManagement.insertNewCustomersSetting(_dbHelper.getWritableDatabase(),holder);

        }
        String a = Character.toString(CustomersTableMagagement.getFirstName(_dbHelper.getReadableDatabase(), custId).charAt(0));
        String b = Character.toString(CustomersTableMagagement.getLastName(_dbHelper.getReadableDatabase(), custId).charAt(0));
        names.add(a + b);
        payment.add(holder);
    }

    public double getQtyOfCustomer(String day, String custId) {
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

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
