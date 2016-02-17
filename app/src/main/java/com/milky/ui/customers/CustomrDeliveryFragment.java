package com.milky.ui.customers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.DateQuantityModel;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.DeliveryTableManagement;
import com.tyczj.extendedcalendarview.ExtcalCustomerSettingTableManagement;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import com.milky.R;

import java.math.BigDecimal;
import java.text.ParseException;
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
    private DatabaseHelper db;
    private ExtcalDatabaseHelper _exDb;
    private String selected_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _exDb = new ExtcalDatabaseHelper(getActivity());
        View view = inflater.inflate(R.layout.calender_layout, container, false);
        _mCalenderView = (ExtendedCalendarView) view.findViewById(R.id.calendar);
//        _mCalenderView.setTotalQuantity(CustomersTableMagagement.getTotalMilkQuantytyForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
//                getActivity().getIntent().getStringExtra("cust_id")));
//        _mCalenderView.customersMilkQuantity(DeliveryTableManagement.getMilkQuantityofCustomer(_exDb.getReadableDatabase(),
//                getActivity().getIntent().getStringExtra("cust_id")));
        custId = getActivity().getIntent().getStringExtra("cust_id");
        _mCalenderView.setForCustomersDelivery(true);

        db = AppUtil.getInstance().getDatabaseHandler();

        initResources();

//        if (totalData.size() > 0) {
//            _mCalenderView.setForCustomersDelivery(true);
//            _mCalenderView.customersMilkQuantity(totalData);
//        }
        return view;

    }

    private void initResources() {
        getTotalQuantity();
        AppUtil.getInstance().getDatabaseHandler().close();
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
                if ((ExtcalCustomerSettingTableManagement.isHasDataForDay(_exDb.getReadableDatabase(), selected_date))
                        && day.getDay() <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && Calendar.getInstance().get(Calendar.MONTH) == day.getMonth() && Calendar.getInstance().get(Calendar.YEAR) == day.getYear()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    final AlertDialog dialog = alertBuilder.create();
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view1 = inflater.inflate(R.layout.edit_quantity_popup, null, false);
                    dialog.setView(view1);

                    final EditText quantity = (EditText) view1.findViewById(R.id.milk_quantity);
                    quantity.setHint("Quantity");
                    final TextView title = (TextView) view1.findViewById(R.id.title);
                    title.setText("Edit Quantity");
                    final TextInputLayout quantity_layout = (TextInputLayout) view1.findViewById(R.id.quantity_layout);
                    quantity.setText(String.valueOf(totalData.get(day.getDay() - 1).getCalculatedQuqantity()));
                    ((Button) view1.findViewById(R.id.save)).setText("Save");
                    ((Button) view1.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (quantity.getText().toString().equals("")) {
                                quantity_layout.setError("Enter quantity!");
                            } else {
                                ExtcalVCustomersList holder = new ExtcalVCustomersList();
                                holder.setQuantity(quantity.getText().toString());
                                holder.setStart_date(selected_date);
                                holder.setCustomerId(custId);
                                if (DeliveryTableManagement.isHasData(_exDb.getReadableDatabase(),
                                        custId, selected_date)) {

                                    DeliveryTableManagement.updateCustomerDetail(_exDb.getWritableDatabase(), holder, "");
                                } else
                                    DeliveryTableManagement.insertCustomerDetail(_exDb.getWritableDatabase(), holder, "", Constants.ACCOUNT_ID);

                                DateQuantityModel holder1 = new DateQuantityModel();
                                holder1.setDeliveryDate(selected_date);
                                holder1.setCalculatedQuqantity(round(Double.parseDouble(quantity.getText().toString()), 1));
                                totalData.set(day.getDay() - 1, holder1);
                                _mCalenderView.refresh();
                                dialog.hide();
                            }

                        }
                    });
                    ((Button) view1.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                    dialog.show();
                }
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
            quantity = getDeliveryOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
            DateQuantityModel holder = new DateQuantityModel();
            holder.setDeliveryDate(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
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
        if (_exDb.isTableNotEmpty("delivery")) {
            if (DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_exDb.getReadableDatabase(), day, custId) == 0) {
                if (_exDb.isTableNotEmpty("customers")) {
                    //TODO ExtCal SETTINGS DB
                    //                    qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
                    //                            , custId);
                    qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
                            , custId);

                }
            } else
                qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_exDb.getReadableDatabase(), day, custId);


        } else if (_exDb.isTableNotEmpty("customers")) {

//            qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
//                    , custId);
            //TODO ExtCal SETTINGS DB
            qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
                    , custId);

        }


        return qty;
    }
}