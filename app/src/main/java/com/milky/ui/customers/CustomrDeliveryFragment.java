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

import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import com.milky.R;

/**
 * Created by Neha on 11/19/2015.
 */
public class CustomrDeliveryFragment extends Fragment {
    private ExtendedCalendarView _mCalenderView;
    private TextInputLayout bill_amount_layout;
    private int dataCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calender_layout, container, false);
        _mCalenderView = (ExtendedCalendarView) view.findViewById(R.id.calendar);
        _mCalenderView.setTotalQuantity(CustomersTableMagagement.getTotalMilkQuantytyForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
                getActivity().getIntent().getStringExtra("cust_id")));
        _mCalenderView.customersMilkQuantity(DeliveryTableManagement.gwtMilkQuantityofCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(),
                getActivity().getIntent().getStringExtra("cust_id")));
        _mCalenderView.setForCustomersDelivery(true);

        initResources();
        return view;

    }

    private void initResources() {
        _mCalenderView.setQuantity("2L");
        _mCalenderView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapterView, View view, int i, long l, Day day) {
                Constants.SELECTED_DAY = day;
                Constants.QUANTITY_UPDATED_DAY = String.valueOf(day.getDay());
                Constants.QUANTITY_UPDATED_MONTH = String.valueOf(day.getMonth());
                Constants.QUANTITY_UPDATED_YEAR = String.valueOf(day.getYear());
                Constants.DELIVERY_DATE = String.valueOf(day.getDay()) + "-" + String.valueOf(day.getMonth()) + "-" + String.valueOf(day.getYear());
                String balance = CustomersTableMagagement.getBalanceForCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), getActivity().getIntent().getStringExtra("cust_id"));
                onCreateDialog(balance);
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
}