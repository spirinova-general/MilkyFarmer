package com.milky.ui.customers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.tyczj.extendedcalendarview.DateQuantityModel;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import com.milky.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Neha on 11/19/2015.
 */
public class CustomrDeliveryFragment extends Fragment {
    private ExtendedCalendarView _mCalenderView;
    private TextInputLayout bill_amount_layout;
    private int dataCount = 0;
    private String custId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calender_layout, container, false);
        _mCalenderView = (ExtendedCalendarView) view.findViewById(R.id.calendar);
        _mCalenderView.setTotalQuantity(CustomersTableMagagement.getTotalMilkQuantytyForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
                getActivity().getIntent().getStringExtra("cust_id")));
        _mCalenderView.customersMilkQuantity(DeliveryTableManagement.getMilkQuantityofCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
                getActivity().getIntent().getStringExtra("cust_id")));
        custId = getActivity().getIntent().getStringExtra("cust_id");
        _mCalenderView.setForCustomersDelivery(true);

        initResources();
        if(totalData.size()>0)
        {
            _mCalenderView.setForCustomersDelivery(true);
            _mCalenderView.customersMilkQuantity(totalData);
        }
        return view;

    }

    private void initResources() {
        getTotalQuantity();
        _mCalenderView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapterView, View view, int i, long l, Day day) {
//                Constants.SELECTED_DAY = day;
//                Constants.QUANTITY_UPDATED_DAY = String.valueOf(day.getDay());
//                Constants.QUANTITY_UPDATED_MONTH = String.valueOf(day.getMonth());
//                Constants.QUANTITY_UPDATED_YEAR = String.valueOf(day.getYear());
//                Constants.DELIVERY_DATE = String.valueOf(day.getDay()) + "-" + String.valueOf(day.getMonth()) + "-" + String.valueOf(day.getYear());
//                String balance = CustomersTableMagagement.getBalanceForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), getActivity().getIntent().getStringExtra("cust_id"));
//                onCreateDialog(balance);
//                String month = new DateFormatSymbols().getMonths()[day.getMonth()];
//                Intent intent = new Intent(getActivity(), CustomersList.class).putExtra("day", day.getDay()).putExtra("month", month);
//                startActivity(intent);

            }
        });
    }

    protected void onCreateDialog(String stringData) {


        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.clear_bill_popup);
        dialog.setTitle("Clear Bill");

        // set the custom dialog components
        final EditText billamount = (EditText) dialog.findViewById(R.id.bill_amount);
        bill_amount_layout = (TextInputLayout) dialog.findViewById(R.id.bill_amount_layout);
        billamount.setText(stringData);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        billamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    dataCount++;
                    bill_amount_layout.setError(null);
                } else {
                    bill_amount_layout.setError("This cannot be empty");
                }
            }
        });

        // if button is clicked, close the custom dialog
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mCalenderView.setQuantity(billamount.getText().toString());
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public static ArrayList<DateQuantityModel> totalData = new ArrayList<>();
    public static double quantity = 0;
    static Calendar cal = Calendar.getInstance();
    public static ArrayList<String> custIds = new ArrayList<>();


    public ArrayList<DateQuantityModel> getTotalQuantity() {
        totalData.clear();
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
            quantity = getDeliveryOfCustomer(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
            DateQuantityModel holder = new DateQuantityModel();
            holder.setDeliveryDate(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
            holder.setCalculatedQuqantity(round(quantity, 1));
            totalData.add(holder);
        }


        return totalData;
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
    private String d = "";
    public double getDeliveryOfCustomer(String day) {
        double qty = 0;
        double adjustedQty = 0;
        if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            if (DeliveryTableManagement.getQuantityOfDayByDateForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day, custId) == 0) {
                if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {

                    qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
                            , custId);

                }
            }
            else
                qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day, custId);


        }
        else
        if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {

            qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
                    , custId);

        }


        return qty;
    }
}