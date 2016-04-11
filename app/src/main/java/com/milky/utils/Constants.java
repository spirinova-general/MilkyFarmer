package com.milky.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.tyczj.extendedcalendarview.Day;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Neha on 12/2/2015.
 */
public class Constants {
    public static Day SELECTED_DAY;

    public static String DELIVERY_DATE = "";
    public static boolean CUSTOMER_ADDED = false;
    public static SimpleDateFormat work_format = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat _display_format = new SimpleDateFormat("dd-MMM-yyyy");
    public static SimpleDateFormat api_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    public static SimpleDateFormat api_format_other = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static boolean validArea = false;
    public static String OTP = "";
    public static int selectedAreaId = -1;
    public static String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static boolean REFRESH_CALANDER = false,REFRESH_CUSTOMERS=false,REFRESH_BILL=false;
    public static boolean REFRESH_DELIVRY_CALANDER = false;
    public static JSONObject API_RESPONCE = null;
    public static Boolean TIME_OUT = false;
    static Calendar cal = Calendar.getInstance();
    public static List<Integer> selectedcustIds;
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
//Send SMS to Customer
public static void SendSmsTouser(String mob, final String sms, final OnTaskCompleteListner activity) {
    String append = "?mobile=" + mob + "&message=" + sms;
    HttpAsycTask dataTask = new HttpAsycTask();
    dataTask.runRequest(ServerApis.SMS_API_ROOT + append, null, activity, false, null);
}

//    public static double getQtyOfCustomer(String day, String custId) {
//        double qty = 0;
//        ExtcalDatabaseHelper _exDb = new ExtcalDatabaseHelper(AppUtil.getInstance());
//        if (_exDb.isTableNotEmpty("delivery")) {
//                   qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(_exDb.getReadableDatabase(), day, custId);
//            if(!DeliveryTableManagement.isFromDelivery && _exDb.isTableNotEmpty("customers")) {
//                    qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
//                            , custId);
//                }
//        } else if (_exDb.isTableNotEmpty("customers")) {
//            qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), day
//                    , custId);
//        }
//        DeliveryTableManagement.isFromDelivery=false;
//
//        return qty;
//    }

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
    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

//    public static double getTotalDeliveriesForMonth(String date) {
//        double qty = 0,adjustQty=0;
//        ExtcalDatabaseHelper _exDb = new ExtcalDatabaseHelper(AppUtil.getInstance());
//        if (_exDb.isTableNotEmpty("delivery")) {
//            qty += DeliveryTableManagement.getQuantityOfDayByDate(_exDb.getReadableDatabase(), date);
//        }
//        if(_exDb.isTableNotEmpty("customers"))
//            if(DeliveryTableManagement.custIds.size()>0)
//        for(int i=0; i<DeliveryTableManagement.custIds.size();++i)
//        adjustQty += ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(_exDb.getReadableDatabase(), date,DeliveryTableManagement.custIds.get(i));
//        qty += ExtcalCustomerSettingTableManagement.getAllCustomersById(_exDb.getReadableDatabase(), date)-adjustQty;
//
//
//        return qty;
//    }
//

//public static void calculateDeliveryTotal()
//{
//    List<Double> data = new ArrayList<>();
//    for(int i=1; i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH); ++i)
//    {
//        data.add(Constants.getTotalDeliveriesForMonth(String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) +
//                "-" + String.format("%02d", i)));
//    }
//    totalDeliveryData=data;
//}

}
