package com.milky.service.databaseutils;

import com.milky.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lead1 on 3/22/2016.
 */
public class DeliveryService {
    public double getTotalDeliveriesForMonth(String date) {
        double qty = 0, adjustQty = 0;
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            qty += DeliveryTableManagement.getQuantityOfDayByDate(db.getReadableDatabase(), date);
        }
        if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS))
            if (DeliveryTableManagement.custIds!=null &&  DeliveryTableManagement.custIds.size() > 0)
                for (int i = 0; i < DeliveryTableManagement.custIds.size(); ++i)
                    adjustQty += CustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), date, DeliveryTableManagement.custIds.get(i));
        qty += CustomerSettingTableManagement.getAllCustomersById(db.getReadableDatabase(), date) - adjustQty;


        return qty;
    }

    public static List<Double> totalDeliveryData;

    public List<Double> calculateDeliveryTotal(int maxDay, int month, int year,boolean isForCustomers) {
        List<Double> data = new ArrayList<>();
        for (int i = 1; i <= maxDay; ++i) {
            if(!isForCustomers) {
                data.add(getTotalDeliveriesForMonth(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i)));
            }
            else
            {
                data.add(calculateDeliveryForCustomers(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i)));
            }
        }
        return data;
    }
    public  static String selectedCustomer="";
    private double calculateDeliveryForCustomers(String date)
    {
        double qty = 0, adjustQty = 0;
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            if (DeliveryTableManagement.getQuantityOfDayByDateForCustomer(db.getReadableDatabase(), date, selectedCustomer) == 0) {
                if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {
                    //TODO ExtCal SETTINGS DB

                    qty = CustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), date
                            , selectedCustomer);

                }
            } else
                qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(db.getReadableDatabase(), date, selectedCustomer);


        } else if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {


            //TODO ExtCal SETTINGS DB
            qty = CustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), date
                    , selectedCustomer);

        }
        return qty;
    }
}
