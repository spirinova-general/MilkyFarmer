package com.milky.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.serverapi.SyncDataService;
import com.tyczj.extendedcalendarview.DateQuantityModel;
import com.tyczj.extendedcalendarview.DeliveryTableManagement;
import com.tyczj.extendedcalendarview.ExtcalCustomerSettingTableManagement;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
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
    private static ExtcalDatabaseHelper _exDb;

    @Override
    public void onCreate() {
        super.onCreate();
// Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
        _instance = this;
        _exDb = new ExtcalDatabaseHelper(_instance);
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

    private static BigDecimal previousQty = null;

    public class GetQuantity extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            totalData.clear();
            //TODO ExtCal SETTINGS DB
//            ArrayList<String> dates = CustomerSettingTableManagement.getDates(getInstance().getDatabaseHandler().getReadableDatabase());
            ArrayList<String> dates = ExtcalCustomerSettingTableManagement.getDates(new ExtcalDatabaseHelper(getInstance()).getReadableDatabase());

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

            Constants.REFRESH_CALANDER = false;
            return null;
        }
    }

//    public static ArrayList<DateQuantityModel> getTotalQuantity() {
//        totalData.clear();
//        ArrayList<String> dates = CustomerSettingTableManagement.getDates(getInstance().getDatabaseHandler().getReadableDatabase());
//        for (int j = 0; j < dates.size(); ++j) {
//            Calendar calendar = Calendar.getInstance();
//            Date date;
//            try {
//                date = Constants.work_format.parse(dates.get(j));
//                calendar.setTime(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
//                quantity = getDeliveryOfCustomer(calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
//                DateQuantityModel holder = new DateQuantityModel();
//                holder.setDeliveryDate(calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
//                holder.setCalculatedQuqantity(round(quantity, 1));
//
////                 if(previousQty==null)
////                 {
////                     previousQty = round(quantity, 1);
////                 }
////
////                if (quantity == 0 && previousQty != null) {
////                    holder.setCalculatedQuqantity(previousQty);
////                    previousQty=null;
////                }
//
//                totalData.add(holder);
//
//            }
//        }
//        AppUtil.getInstance().getDatabaseHandler().close();
//
//        Constants.REFRESH_CALANDER = false;
//        return totalData;
//    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static double getDeliveryOfCustomer(String day) {
        DeliveryTableManagement.custIds.clear();
        double qty = 0;
        double adjustedQty = 0;
//        if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_DELIVERY))
        if (DeliveryTableManagement.isDeletedCustomer(_exDb.getReadableDatabase(), day))
            qty = DeliveryTableManagement.getQuantityOfDayByDate(_exDb.getReadableDatabase(), day);

        if (_exDb.isTableNotEmpty("customers")) {
            if (DeliveryTableManagement.custIds.size() > 0)
                for (int i = 0; i < DeliveryTableManagement.custIds.size(); i++)
                    //TODO ExtCal SETTINGS DB
                    adjustedQty += ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
                            ,DeliveryTableManagement. custIds.get(i));
//            adjustedQty += CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
//                    , custIds.get(i));
//TODO ExtCal SETTINGS DB
//            qty += CustomerSettingTableManagement.getAllCustomersByDay(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day) - adjustedQty;
            qty += ExtcalCustomerSettingTableManagement.getAllCustomersByDay(_exDb.getReadableDatabase(), day) - adjustedQty;

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
                        .setSmallIcon(R.drawable.k_logo)
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

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically


        extractLogToFile();
//        System.exit(1); // kill off the crashed app
    }

    private String extractLogToFile() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        String path = Environment.getExternalStorageDirectory() + "/" + "milky/";
        String fullName = path + "log";

        // Extract to file.
        File file = new File(fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time milky:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader(process.getInputStream());

            // write output stream
            writer = new FileWriter(file);
            writer.write("Android version: " + Build.VERSION.SDK_INT + "\n");
            writer.write("Device: " + model + "\n");
            writer.write("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do {
                int n = reader.read(buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write(buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
        } catch (IOException e) {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        return fullName;
    }
}