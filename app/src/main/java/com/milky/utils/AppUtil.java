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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.serverapi.SyncDataService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
// Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
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


    public static double quantity = 0;


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

    private boolean isConnected = false;

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {

                            isConnected = true;

                        }
                        return true;
                    }
                }
            }
        }
        isConnected = false;
        return false;
    }


}