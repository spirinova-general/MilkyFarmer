package com.milky.service.serverapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;

/**
 * Created by Lead1 on 4/12/2016.
 */
public class BroadcastCalendar extends BroadcastReceiver {
    private Intent intent;
    private Activity handler;
    private ProgressDialog pBar;
    private int custId=-1;

    public BroadcastCalendar(Activity handler) {
        this.handler = handler;
        this.custId=-1;
    }
    public BroadcastCalendar(Activity handler,int custId) {
        this.handler = handler;
        this.custId=custId;
    }
    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.intent = intent;

        handler.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DeliveryService service = new DeliveryService();
                if(custId <0)
                    new ExtendedCalendarView(context).updateQuantityList(service.getMonthlyDeliveryOfAllCustomers(intent.getIntExtra("month", 0), intent.getIntExtra("year", 0)));
                else
                    new ExtendedCalendarView(context).updateQuantityList(new DeliveryService().getMonthlyDeliveryOfCustomer(custId, intent.getIntExtra("month", 0), intent.getIntExtra("year", 0)));
            }
        });

    }


}
