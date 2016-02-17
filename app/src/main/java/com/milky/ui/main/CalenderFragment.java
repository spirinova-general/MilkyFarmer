package com.milky.ui.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.milky.R;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.customers.CustomersList;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.UserPrefrences;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtcalCustomerSettingTableManagement;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;

/**
 * Created by Neha on 11/17/2015.
 */
public class CalenderFragment extends Fragment {
    private ExtendedCalendarView _mCalenderView;
    private SharedPreferences _prefrences;
    private SharedPreferences.Editor _editor;
    private DatabaseHelper _dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calender_layout, container, false);
        initResources(view);
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        if (Constants.REFRESH_CALANDER) {
            _mCalenderView.setForCustomersDelivery(false);

            _mCalenderView.refresh();
            if (AppUtil.totalData.size() > 0)
                _mCalenderView.totalQuantityCalculated(AppUtil.totalData);
            Constants.REFRESH_CALANDER = false;

        }


    }

    View viewLayout;

    private void initResources(View view) {
        viewLayout = view;

        _prefrences = AppUtil.getInstance().getPrefrences();
        _editor = _prefrences.edit();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        _mCalenderView = (ExtendedCalendarView) viewLayout.findViewById(R.id.calendar);
        _mCalenderView.setForCustomersDelivery(false);

        _mCalenderView.refresh();
        if (AppUtil.totalData.size() > 0)
            _mCalenderView.totalQuantityCalculated(AppUtil.totalData);
        _mCalenderView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapterView, View view, int i, long l, Day day) {
                Constants.SELECTED_DAY = day;
                Constants.DELIVERY_DATE = day.getYear() + "-" + String.format("%02d", day.getMonth() + 1) + "-" +
                        String.format("%02d", day.getDay());
                //TODO ExtCal SETTINGS DB
                if ((ExtcalCustomerSettingTableManagement.isHasDataForDay(new ExtcalDatabaseHelper(getActivity()).getReadableDatabase(), Constants.DELIVERY_DATE))
                        && day.getDay()<=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)  && Calendar.getInstance().get(Calendar.MONTH) == day.getMonth() && Calendar.getInstance().get(Calendar.YEAR) == day.getYear()) {
                    Intent intent = new Intent(getActivity(), CustomersList.class);
                    startActivity(intent);
                }
            }
        });
         /*
        * Check if user is newly registered...
        * */
        if (_prefrences.contains(UserPrefrences.NEW_USER)) {
            _mCalenderView.setRegistrationDate(_prefrences.getInt(UserPrefrences.NEW_USER, 0));

        } else {
            Calendar cl = Calendar.getInstance();
            _editor.putInt(UserPrefrences.NEW_USER, cl.get(Calendar.DAY_OF_MONTH));
            _editor.commit();
            _mCalenderView.setRegistrationDate(cl.get(Calendar.DAY_OF_MONTH));
            _mCalenderView.setRegistrationYear(cl.get(Calendar.YEAR));
            _mCalenderView.setRegistrationMonth(cl.get(Calendar.MONTH)) ;
        }
        /*
        * Check if has customers added
        * */
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            _mCalenderView.setQuantity(String.valueOf(CustomersTableMagagement.getTotalMilkQuantyty(_dbHelper.getReadableDatabase())));

        } else
            _mCalenderView.setQuantity("0");
        _dbHelper.close();

    }
}
