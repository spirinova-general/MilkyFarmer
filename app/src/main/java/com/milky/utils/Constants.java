package com.milky.utils;

import com.tyczj.extendedcalendarview.Day;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Neha on 12/2/2015.
 */
public class Constants {
    public static String ACCOUNT_ID = "12345";
    public static Day SELECTED_DAY;
    public static String QUANTITY_UPDATED_DAY = "";
    public static String QUANTITY_UPDATED_MONTH = "";
    public static String QUANTITY_UPDATED_YEAR = "";
    public static String DELIVERY_DATE = "";
    public static boolean CUSTOMER_ADDED=false;
    public static SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
    public static boolean validArea = false;


    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        return df.format(cal.getTime());

    }
}
