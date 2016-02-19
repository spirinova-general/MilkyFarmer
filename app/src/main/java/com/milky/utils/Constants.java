package com.milky.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tyczj.extendedcalendarview.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Neha on 12/2/2015.
 */
public class Constants {
    public static String ACCOUNT_ID = "12345";
    public static Day SELECTED_DAY;

    public static String DELIVERY_DATE = "";
    public static boolean CUSTOMER_ADDED = false;
    public static SimpleDateFormat work_format = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat _display_format = new SimpleDateFormat("dd-MMM-yyyy");
    public static boolean validArea = false;
    public static String OTP = "";
    public static String selectedAreaId = "";
    public static String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static boolean REFRESH_CALANDER = false;
    public static boolean REFRESH_DELIVRY_CALANDER = false;

    static Calendar cal = Calendar.getInstance();

    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.format(cal.getTime());
        return (cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
    }

    public static String generateOTP() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        return result;
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
