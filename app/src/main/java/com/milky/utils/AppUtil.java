package com.milky.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.serverapi.SyncDataService;
import com.tyczj.extendedcalendarview.DateQuantityModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Singleton class for access shared resource in application wide.
 * <p/>
 * Created by Neha on 12/2/2015.
 */
public class AppUtil extends Application {


    private static final String TAG = AppUtil.class.getSimpleName();
    private static AppUtil _instance;
    private DatabaseHelper _dbHandler;
    private SharedPreferences _sharedPRefrences;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
        _dbHandler = new DatabaseHelper(getApplicationContext());
        _sharedPRefrences = getApplicationContext().getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);
        startService(new Intent(getBaseContext(), SyncDataService.class));

    }

    public static synchronized AppUtil getInstance() {
        return _instance;
    }


    public DatabaseHelper getDatabaseHandler() {
        return _dbHandler;
    }

    public SharedPreferences getPrefrences() {
        return _sharedPRefrences;
    }

    public static ArrayList<DateQuantityModel> totalData = new ArrayList<>();
    public static double quantity = 0;
    static Calendar cal = Calendar.getInstance();
    public static ArrayList<String> custIds = new ArrayList<>();
    private static BigDecimal previousQty = null;

    public static ArrayList<DateQuantityModel> getTotalQuantity() {
        totalData.clear();
        ArrayList<String> dates = CustomerSettingTableManagement.getDates(getInstance().getDatabaseHandler().getReadableDatabase());
        for (int j = 0; j < dates.size(); ++j) {
            Calendar calendar = Calendar.getInstance();
            Date date;
            try {
                date = Constants.work_format.parse(dates.get(j));
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
                quantity = getDeliveryOfCustomer(calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                DateQuantityModel holder = new DateQuantityModel();
                holder.setDeliveryDate(calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                holder.setCalculatedQuqantity(round(quantity, 1));

//                 if(previousQty==null)
//                 {
//                     previousQty = round(quantity, 1);
//                 }
//
//                if (quantity == 0 && previousQty != null) {
//                    holder.setCalculatedQuqantity(previousQty);
//                    previousQty=null;
//                }

                totalData.add(holder);

            }
        }
        AppUtil.getInstance().getDatabaseHandler().close();


        return totalData;
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static double getDeliveryOfCustomer(String day) {
        custIds.clear();
        double qty = 0;
        double adjustedQty = 0;
//        if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_DELIVERY))
        if (DeliveryTableManagement.isDeletedCustomer(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day))
            qty = DeliveryTableManagement.getQuantityOfDayByDate(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day);

        if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {
            if (custIds.size() > 0)
                for (int i = 0; i < custIds.size(); i++)
                    adjustedQty += CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
                            , custIds.get(i));

            qty += CustomerSettingTableManagement.getAllCustomersByDay(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day) - adjustedQty;

        }
        return qty;
    }


    public static void showNotification(Context context, String title, String content, Intent intent) {
        intent.setFlags(0);
        int notificationId = (new Random()).nextInt();
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, notificationId, intent, 0);
        Constants.OTP = Constants.generateOTP();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.main_logo)
                        .setContentTitle(title).setAutoCancel(true).setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                        .setContentText(content.replaceAll("<[^<>]+>", "") + Constants.OTP);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
//        if (sound != null && sound.trim().length() > 0) {
//            notification.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + sound);
//        }
        manager.notify(notificationId, notification);
    }

    public CountDownTimer timer;

    public void startTimer() {
        timer = new CountDownTimer(14640000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {

                Constants.OTP = "";

            }
        }.start();
    }

    public void cancelTimer(Activity activity) {
        timer.cancel();
        Toast.makeText(activity, "OTP has been resent", Toast.LENGTH_SHORT).show();
    }
}