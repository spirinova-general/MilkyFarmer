package com.milky.ui.customers;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import com.milky.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Neha on 11/19/2015.
 */
public class CustomrDeliveryFragment extends Fragment {
    private ExtendedCalendarView _mCalenderView;
    private TextInputLayout bill_amount_layout;
    private int dataCount = 0;
    private String custId = "";
    private DatabaseHelper db;
//    private ExtcalDatabaseHelper _exDb;
    private String selected_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        _exDb = new ExtcalDatabaseHelper(getActivity());
        View view = inflater.inflate(R.layout.calender_layout, container, false);
        _mCalenderView = (ExtendedCalendarView) view.findViewById(R.id.calendar);
//        _mCalenderView.setTotalQuantity(CustomersTableMagagement.getTotalMilkQuantytyForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
//                getActivity().getIntent().getStringExtra("cust_id")));
//        _mCalenderView.customersMilkQuantity(DeliveryTableManagement.getMilkQuantityofCustomer(_exDb.getReadableDatabase(),
//                getActivity().getIntent().getStringExtra("cust_id")));

        custId = getActivity().getIntent().getStringExtra("cust_id");
        _mCalenderView.setForCustomersDelivery(true);
        _mCalenderView.setCustomerId(custId);
        db = AppUtil.getInstance().getDatabaseHandler();

        initResources();

//        if (totalData.size() > 0) {
//            _mCalenderView.setForCustomersDelivery(true);
//            _mCalenderView.customersMilkQuantity(totalData);
//        }
        return view;

    }

    private AlertDialog dialog;

    private void initResources() {
        Calendar cal = Calendar.getInstance();
//        _mCalenderView.calculateDeliveryTotal(cal.getActualMaximum(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
//        _mCalenderView.updateQuantityList(_mCalenderView.totalDeliveryData);
//        AppUtil.getInstance().getDatabaseHandler().close();
//        totalData = ExtendedCalendarView.totalDeliveryData;
        _mCalenderView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapterView, View view, final int i, long l, final Day day) {
                selected_date = day.getYear() + "-" + String.format("%02d", day.getMonth() + 1) + "-" +
                        String.format("%02d", day.getDay());

                Calendar deliveryDate = Calendar.getInstance();
                try {
                    deliveryDate.setTime(Constants.work_format.parse(getActivity().getIntent().getStringExtra("start_delivery_date")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                if ((ExtcalCustomerSettingTableManagement.isHasDataForDayOfCust(_exDb.getReadableDatabase(), selected_date, custId))
//                        && day.getDay() <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && Calendar.getInstance().get(Calendar.MONTH) == day.getMonth() && Calendar.getInstance().get(Calendar.YEAR) == day.getYear()) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
//                    dialog = alertBuilder.create();
//                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View view1 = inflater.inflate(R.layout.edit_quantity_popup, null, false);
//                    dialog.setView(view1);
//
//                    final EditText quantity = (EditText) view1.findViewById(R.id.milk_quantity);
//                    quantity.setHint("Quantity");
//                    final TextView title = (TextView) view1.findViewById(R.id.title);
//                    title.setText("Edit Quantity");
//                    final TextInputLayout quantity_layout = (TextInputLayout) view1.findViewById(R.id.quantity_layout);
//                    quantity.setText(String.valueOf(totalData.get(day.getDay() - 1)));
//                    ((Button) view1.findViewById(R.id.clear)).setText("Save");
//                    ((Button) view1.findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (quantity.getText().toString().equals(""))
//                                quantity_layout.setError("Enter quantity!");
//                            else {
//                                VCustomers holder = new VCustomers();
//                                holder.setQuantity(quantity.getText().toString());
//                                holder.setStart_date(selected_date);
//                                holder.setCustomerId(custId);
//                                if (DeliveryTableManagement.isHasData(_exDb.getReadableDatabase(),
//                                        custId, selected_date))
//                                    DeliveryTableManagement.updateCustomerDetail(_exDb.getWritableDatabase(), holder);
//                                else
//                                    DeliveryTableManagement.insertCustomerDetail(_exDb.getWritableDatabase(), holder, Constants.ACCOUNT_ID);
//
//                                totalData.set(day.getDay() - 1, Double.parseDouble(quantity.getText().toString()));
//                                _mCalenderView.refresh();
//                                Constants.REFRESH_CALANDER = true;
//                                dialog.dismiss();
//                            }
//
//                        }
//                    });
//                    ((Button) view1.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    if (dialog != null)
//                        dialog.show();
//                }
            }
        });
    }


    public static List<Double> totalData;
    public static double quantity = 0;
    static Calendar cal = Calendar.getInstance();

//    public ArrayList<DateQuantityModel> getTotalQuantity() {
//        totalData.clear();
//        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
//            quantity = getDeliveryOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
//            DateQuantityModel holder = new DateQuantityModel();
//            holder.setDeliveryDate(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
//            holder.setCalculatedQuqantity(round(quantity, 1));
//            totalData.add(holder);
//        }
//
//
//        return totalData;
//    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private String d = "";

//    public double getDeliveryOfCustomer(String day) {
//        double qty = 0;
//        double adjustedQty = 0;
//        if (_exDb.isTableNotEmpty("delivery")) {
//            if (DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_exDb.getReadableDatabase(), day, custId) == 0) {
//                if (_exDb.isTableNotEmpty("customers")) {
//                    //TODO ExtCal SETTINGS DB
//                    //                    qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
//                    //                            , custId);
//                    qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
//                            , custId);
//
//                }
//            } else
//                qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_exDb.getReadableDatabase(), day, custId);
//
//
//        } else if (_exDb.isTableNotEmpty("customers")) {
//
////            qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
////                    , custId);
//            //TODO ExtCal SETTINGS DB
//            qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
//                    , custId);
//
//        }
//
//
//        return qty;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null)
            dialog.dismiss();
    }
}