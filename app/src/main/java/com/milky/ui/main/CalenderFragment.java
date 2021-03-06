package com.milky.ui.main;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.ui.customers.DeliveryActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.UserPrefrences;
import com.milky.service.serverapi.BroadcastCalendar;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neha on 11/17/2015.
 */
public class CalenderFragment extends Fragment {
    private ExtendedCalendarView _mCalenderView;
    private SharedPreferences _prefrences;
    private SharedPreferences.Editor _editor;
    private DatabaseHelper _dbHelper;
    private View viewLayout, view = null;
    private com.milky.service.databaseutils.serviceclasses.DeliveryService deliveryService;
    private BroadcastCalendar receiver;
    private IntentFilter intentFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.calender_layout, container, false);
            initResources(view);
        }
        return view;
    }

    private class UpdataCalander extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _mCalenderView.setForCustomersDelivery(false);
                    _mCalenderView.refresh();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Constants.REFRESH_CALANDER = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, intentFilter);
        if (Constants.REFRESH_CALANDER) {
            Calendar cal = Calendar.getInstance();
            DeliveryService service = new DeliveryService();
            _mCalenderView.updateQuantityList(service.getMonthlyDeliveryOfAllCustomers(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)));
            new UpdataCalander().execute();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void initResources(View view) {
        viewLayout = view;
        _prefrences = AppUtil.getInstance().getPrefrences();
        _editor = _prefrences.edit();
        deliveryService = new com.milky.service.databaseutils.serviceclasses.DeliveryService();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        _mCalenderView = (ExtendedCalendarView) viewLayout.findViewById(R.id.calendar);
        receiver = new BroadcastCalendar(getActivity());
        intentFilter = new IntentFilter("com.android.USER_ACTION");
        _mCalenderView.setForCustomersDelivery(false);


        _mCalenderView.setOnDayClickListener(
                new ExtendedCalendarView.OnDayClickListener() {
                    @Override
                    public void onDayClicked(AdapterView<?> adapterView, View view, int i, long l, Day day) {
                        Constants.SELECTED_DAY = day;
                        try {
                            Calendar cal = Calendar.getInstance();
                            Date today = cal.getTime();

                            String dateStr = Utils.ToDateString(day.getDay(), day.getMonth() + 1, day.getYear());
                            Date clickedDate = Utils.FromDateString(dateStr);

                            if (Utils.BeforeOrEqualsDate(clickedDate, today)) {
                                Intent intent = new Intent(getActivity(), DeliveryActivity.class);
                                intent.putExtra("deliveryDate", dateStr);
                                startActivity(intent);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

        );
         /*
        * Check if user is newly registered...
        * */
        if (_prefrences.contains(UserPrefrences.NEW_USER))
            _mCalenderView.setRegistrationDate(_prefrences.getInt(UserPrefrences.NEW_USER, 0));
        else

        {
            Calendar cl = Calendar.getInstance();
            _editor.putInt(UserPrefrences.NEW_USER, cl.get(Calendar.DAY_OF_MONTH));
            _editor.commit();
            _mCalenderView.setRegistrationDate(cl.get(Calendar.DAY_OF_MONTH));
            _mCalenderView.setRegistrationYear(cl.get(Calendar.YEAR));
            _mCalenderView.setRegistrationMonth(cl.get(Calendar.MONTH));
        }

        _dbHelper.close();

    }
}
