package com.milky.service.serverapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;

/**
 * Created by Lead1 on 4/12/2016.
 */
public class BroadcastCalendar extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DeliveryService service = new DeliveryService();
        new ExtendedCalendarView(context).updateQuantityList(service.getMonthlyDeliveryOfAllCustomers(intent.getIntExtra("month", 0), intent.getIntExtra("year", 0)));
    }
}
