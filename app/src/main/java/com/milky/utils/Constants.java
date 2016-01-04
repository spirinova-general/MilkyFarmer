package com.milky.utils;

import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.tyczj.extendedcalendarview.DateQuantityModel;
import com.tyczj.extendedcalendarview.Day;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    public static boolean CUSTOMER_ADDED = false;
    public static SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
    public static boolean validArea = false;


    static Calendar cal = Calendar.getInstance();

    public static String getCurrentDate() {

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        df.format(cal.getTime());
        return (String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", cal.get(Calendar.YEAR)));
    }


}
