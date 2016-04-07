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

import com.milky.service.core.CustomersSetting;
import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.serviceclasses.CustomersSettingService;
import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import com.milky.R;

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
    private int custId = 0;
    private String selected_date;
    private CustomersSetting settingData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        _exDb = new ExtcalDatabaseHelper(getActivity());
        View view = inflater.inflate(R.layout.calender_layout, container, false);
        _mCalenderView = (ExtendedCalendarView) view.findViewById(R.id.calendar);
//        _mCalenderView.setTotalQuantity(CustomersTableMagagement.getTotalMilkQuantytyForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
//                getActivity().getIntent().getStringExtra("cust_id")));
//        _mCalenderView.customersMilkQuantity(DeliveryTableManagement.getMilkQuantityofCustomer(_exDb.getReadableDatabase(),
//                getActivity().getIntent().getStringExtra("cust_id")));

        custId = getActivity().getIntent().getIntExtra("cust_id", 0);
        settingData = new CustomersSettingService().getByCustId(custId, Constants.getCurrentDate());
        _mCalenderView.setForCustomersDelivery(true);
        _mCalenderView.setCustomerId(custId);

        initResources();
        _mCalenderView.updateQuantityList(totalData);

//        if (totalData.size() > 0) {
//            _mCalenderView.setForCustomersDelivery(true);
//            _mCalenderView.customersMilkQuantity(totalData);
//        }
        return view;

    }

    private AlertDialog dialog;

    private void initResources() {
        Calendar cal = Calendar.getInstance();
        totalData = new DeliveryService().getMonthlyDeliveryOfCustomer(custId, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
        _mCalenderView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapterView, View view, final int i, long l, final Day day) {
                selected_date = day.getYear() + "-" + String.format("%02d", day.getMonth() + 1) + "-" +
                        String.format("%02d", day.getDay());

                Calendar deliveryDate = Calendar.getInstance();
                try {
                    deliveryDate.setTime(Constants.work_format.parse(settingData.getStartDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if ((new CustomersSettingService().isHasDataForCustoner(selected_date, custId))
                        && day.getDay() <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && Calendar.getInstance().get(Calendar.MONTH) == day.getMonth() && Calendar.getInstance().get(Calendar.YEAR) == day.getYear()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    dialog = alertBuilder.create();
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1 = inflater.inflate(R.layout.edit_quantity_popup, null, false);
                    dialog.setView(view1);

                    final EditText quantity = (EditText) view1.findViewById(R.id.milk_quantity);
                    quantity.setHint("Quantity");
                    final TextView title = (TextView) view1.findViewById(R.id.title);
                    title.setText("Edit Quantity");
                    final TextInputLayout quantity_layout = (TextInputLayout) view1.findViewById(R.id.quantity_layout);
                    quantity.setText(String.valueOf(totalData.get(day.getDay() - 1)));
                    ((Button) view1.findViewById(R.id.clear)).setText("Save");
                    ((Button) view1.findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (quantity.getText().toString().equals(""))
                                quantity_layout.setError("Enter quantity!");
                            else {
                                Delivery holder = new Delivery();
                                holder.setQuantity(Double.parseDouble(quantity.getText().toString()));
                                holder.setDeliveryDate(selected_date);
                                holder.setCustomerId(custId);
                                holder.setDateModified(Constants.getCurrentDate());
                                DeliveryService deliveryService = new DeliveryService();
                                deliveryService.insertOrUpdate(holder);


                                totalData.set(day.getDay() - 1, Double.parseDouble(quantity.getText().toString()));
                                _mCalenderView.refreshAdapter();
                                Constants.REFRESH_CALANDER = true;
                                Constants.REFRESH_BILL=true;
                                dialog.dismiss();
                            }

                        }
                    });
                    ((Button) view1.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    if (dialog != null)
                        dialog.show();
                }
            }
        });
    }


    public static List<Double> totalData;


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