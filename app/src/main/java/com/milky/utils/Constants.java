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
    public static ArrayList<String> custIds = new ArrayList<>();

    static Calendar cal = Calendar.getInstance();

    public static String getCurrentDate() {

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        return df.format(cal.getTime());

    }

    public static ArrayList<DateQuantityModel> totalData = new ArrayList<>();
    public static double quantity = 0;

    public static ArrayList<DateQuantityModel> getTotalQuantity() {
        totalData.clear();
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); ++i) {
            quantity = getDeliveryOfCustomer(String.valueOf(cal.get(Calendar.MONTH)+1) + "-" + String.valueOf(i) + "-" + String.valueOf(cal.get(Calendar.YEAR)));
            DateQuantityModel holder = new DateQuantityModel();
            holder.setDeliveryDate(String.valueOf(cal.get(Calendar.MONTH)+1) + "-" + String.valueOf(i) + "-" + String.valueOf(cal.get(Calendar.YEAR)));
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
