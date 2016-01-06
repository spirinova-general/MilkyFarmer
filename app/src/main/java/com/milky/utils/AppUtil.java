package com.milky.utils;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.serverapi.SyncDataService;
import com.tyczj.extendedcalendarview.DateQuantityModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

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


    public static ArrayList<DateQuantityModel> getTotalQuantity() {
        totalData.clear();
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
            quantity = getDeliveryOfCustomer(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" +String.format("%02d", cal.get(Calendar.YEAR)));
            DateQuantityModel holder = new DateQuantityModel();
            holder.setDeliveryDate(String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i) + "-" +String.format("%02d",cal.get(Calendar.YEAR)));
            holder.setCalculatedQuqantity(round(quantity, 1));
            totalData.add(holder);
        }


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
        if (AppUtil.getInstance().getDatabaseHandler().isTableNotEmpty(TableNames.TABLE_DELIVERY))
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

}