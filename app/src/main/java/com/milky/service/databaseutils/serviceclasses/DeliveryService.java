package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.service.databaseutils.serviceinterface.ICustomersSettings;
import com.milky.service.databaseutils.serviceinterface.IDelivery;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VDelivery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DeliveryService implements IDelivery {

    ICustomers _customerService = new CustomersService();
    ICustomersSettings _customerSettingService = new CustomersSettingService();

    @Override
    public void insert(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultQuantity, delivery.getQuantity());
        values.put(TableColumns.CustomerId, delivery.getCustomerId());
        values.put(TableColumns.DeliveryDate, delivery.getDeliveryDate());
        values.put(TableColumns.Dirty, delivery.getDirty());
        values.put(TableColumns.DateModified, delivery.getDateModified());
        values.put(TableColumns.IsDeleted,delivery.getIsDeleted());
        getDb().insert(TableNames.DELIVERY, null, values);
    }

    @Override
    public void update(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultQuantity, delivery.getQuantity());
        values.put(TableColumns.CustomerId, delivery.getCustomerId());
        values.put(TableColumns.DeliveryDate, delivery.getDeliveryDate());
        values.put(TableColumns.Dirty, delivery.getDirty());
        values.put(TableColumns.DateModified, delivery.getDateModified());
        values.put(TableColumns.IsDeleted,delivery.getIsDeleted());
        getDb().update(TableNames.DELIVERY, values, TableColumns.CustomerId + " ='" + delivery.getCustomerId() + "'" +
                " AND " + TableColumns.DeliveryDate + " ='" + delivery.getDeliveryDate() + "'", null);
    }

    //Umesh doing it in memory for less database hits, remove maxday, startdate its not needed
    @Override
    public List<Double> getMonthlyDeliveryOfAllCustomers(int month, int year) {
        try {
            List<Double> result = new ArrayList<>();
            Calendar start = Calendar.getInstance();
            //Umesh - Get first day and last days of the month...Review this - dont use deprecated APIs
            start.set(year, month, 1);
            //set time zero, so that comparison between dates are not affected by hours
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);
            start.set(Calendar.HOUR_OF_DAY,0);
            Date firstDayOfTheMonth = start.getTime();

            Calendar end = Calendar.getInstance();
            //Set the month and date of calendar as selected from calendar
            end.set(Calendar.MONTH, month);
            //Get last date of the month which is selected from calendar and set that date as end date
            end.set(year,month,end.getActualMaximum(Calendar.DAY_OF_MONTH));
            end.set(Calendar.HOUR_OF_DAY, 0);
            end.set(Calendar.MINUTE, 0);
            end.set(Calendar.SECOND, 0);
            end.set(Calendar.MILLISECOND, 0);

            Date lastDayOfTheMonth = end.getTime();
            List<Customers> customers = _customerService.getCustomersWithinDeliveryRange(null, firstDayOfTheMonth, lastDayOfTheMonth);
           // loop till the start date is before and equal to the end date
            for (Date date = start.getTime(); start.before(end) || start.equals(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                double totalQuantity = 0;
                CustomersSetting fetchedSettings = null;
                for (Customers customer : customers) {
                    CustomersSetting setting = _customerService.getCustomerSetting(customer, date, false, false);
                    if (setting != null) {
                        //If the date already fecthed and from same settings
                        if (fetchedSettings != null && fetchedSettings.getCustomerId()==setting.getCustomerId() && fetchedSettings.getStartDate().equals(setting.getStartDate())) {
                            totalQuantity += setting.getGetDefaultQuantity() - fetchedSettings.getGetDefaultQuantity();
                        } else {
                            totalQuantity += setting.getGetDefaultQuantity();
                            fetchedSettings = setting;
                        }
                    }

                }
                result.add(totalQuantity);
            }
            return result;
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public List<Double> getMonthlyDeliveryOfCustomer(int customerId, int month, int year) {
        try {
            List<Double> result = new ArrayList<Double>();
            Calendar start = Calendar.getInstance();
            start.set(year, month, 1);
            Date firstDayOfTheMonth = start.getTime();
            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date lastDayOfTheMonth = end.getTime();

            Customers customer = _customerService.getCustomerDetail(customerId, true);
            Date date = firstDayOfTheMonth;

            for (;Utils.BeforeOrEqualsDate(date, lastDayOfTheMonth); start.add(Calendar.DATE, 1), date = start.getTime()) {
                CustomersSetting setting = _customerService.getCustomerSetting(customer, date, false, false);
                double quantity =  setting == null? 0: setting.getGetDefaultQuantity();
                result.add(quantity);
            }

            return result;
        } catch (Exception ex) {
            //ALl these should be logged or re thrown when needed....
            return null;
        }
    }

    @Override
    public List<VDelivery> getDeliveryDetails(String day) {
        return getDeliveryDetails(null, day);
    }

    //Umesh use stringbuilder for appending, rather than strings, also select only columns that are needed
    //Corresponding Table column constants should be part of the core classes itself...
    @Override
    public List<VDelivery> getDeliveryDetails(Integer areaId, String day) {
        try {

            Date date = Utils.FromDateString(day);

            List<Customers> customers = _customerService.getCustomersWithinDeliveryRange(areaId, date, date);
            List<VDelivery> result = new ArrayList<>();
            for (Customers customer : customers) {
                VDelivery holder = new VDelivery();
                CustomersSetting setting = _customerService.getCustomerSetting(customer, date, false, false);
                if( setting != null) {
                    holder.setCustomerId(customer.getCustomerId());
                    holder.setQuantity(setting.getGetDefaultQuantity());
                    holder.setAreaId(customer.getAreaId());
                    holder.setFirstname(customer.getFirstName());
                    holder.setLastname(customer.getLastName());
                    result.add(holder);
                }
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }


     /* @Override
    public void insertOrUpdate(Delivery delivery)
    {
        String deliveryDate = delivery.getDeliveryDate();
        String whereClause = TableColumns.StartDate + " ='" + deliveryDate + "'"
                + " AND " + TableColumns.EndDate + " ='" + deliveryDate+"'"
                + " AND " + TableColumns.IsCustomDelivery + " ='1'";

        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + whereClause;

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();

        if (result) {
            ContentValues values = new ContentValues();
            values.put(TableColumns.DefaultQuantity, delivery.getQuantity());
            getDb().update(TableNames.CustomerSetting, values, whereClause, null);
        } else {
            Customers customer = _customerService.getCustomerDetail(delivery.getCustomerId(), true);
            Date date = null;
            CustomersSetting setting = null;

            //Review date parsing later
            try {
             date = Utils.FromDateString(deliveryDate);
             setting = _customerService.getCustomerSetting(customer, date, false);
            }
            catch(Exception ex) {
                return;
            }

            CustomersSetting newSetting = new CustomersSetting();

            newSetting.setCustomerId(setting.getCustomerId());
            newSetting.setDefaultRate(setting.getDefaultRate());
            newSetting.setGetDefaultQuantity(delivery.getQuantity());
            newSetting.setStartDate(deliveryDate);
            newSetting.setEndDate(deliveryDate);
            newSetting.setIsCustomDelivery(true);
            newSetting.setDirty(1);
            _customerSettingService.insert(newSetting);
        }

    }*/
    //Umesh - the dates should have been converted to date in the UI itself, the core classes
    //should have Date rather than string.
    /*private boolean isHasDataForDay(String day, int custId) {
        String selectQuery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate + " ='"
                + day + "'" + " AND "
                + TableColumns.CustomerId + " ='" + custId + "'";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }*/



 /*
  //Get quantity total for some dates, for bill
    public double getTotalQuantityConsumed(int startDate, int maxDay, int month, int year, boolean isForCustomers, int id) {
        double data = 0;
        for (int i = startDate; i <= maxDay; ++i) {
            if (!isForCustomers) {
                data += getTotalDeliveryTillDayforCustomer(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i), id);
            } else {
                data += calculateDeliveryForCustomers(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i), id);
            }
        }
        return data;
    }


    private double getTotalDeliveryByDay(int id, String day) {
        double quantity = 0;

        String selectquery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate + " ='" + day + "'" + " AND " + TableColumns.CustomerId + " ='" + id + "'";


        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                quantity = cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }





    public static List<Integer> custIds;

    private double getTotalDeliveriesForMonth(String date) {
        double qty = 0, adjustQty = 0;
        CustomersSettingService settingService = new CustomersSettingService();
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.DELIVERY)) {
            qty += getQuantityOfDayByDate(date);
        }
        if (db.isTableNotEmpty(TableNames.CustomerSetting))
            if (custIds != null && custIds.size() > 0)
                for (int i = 0; i < custIds.size(); ++i)
                    adjustQty += settingService.getQuantityById(db.getReadableDatabase(), date, custIds.get(i));
        qty += settingService.getTotalQuantity(db.getReadableDatabase(), date) - adjustQty;


        return qty;
    }

    private double calculateDeliveryForCustomers(String date, int id) {
        double qty = 0;
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        CustomersSettingService settingService = new CustomersSettingService();
        if (db.isTableNotEmpty(TableNames.DELIVERY)) {
            if (!isHasDataForDay(date, id)) {
                if (db.isTableNotEmpty(TableNames.CustomerSetting)) {
                    qty = settingService.getQuantityById(db.getReadableDatabase(), date
                            , id);

                }
            } else
                qty = getTotalDeliveryByDay(id, date);


        } else if (db.isTableNotEmpty(TableNames.CustomerSetting)) {

            qty = settingService.getQuantityById(db.getReadableDatabase(), date
                    , id);

        }
        return qty;
    }



    private double getQuantityOfDayByDate(String day) {
        String selectquery = "";
        selectquery = "SELECT * FROM " + TableNames.DELIVERY + " INNER JOIN " + TableNames.CUSTOMER
                + " ON " + TableNames.DELIVERY + "." + TableColumns.CustomerId + " =" + TableNames.CUSTOMER + "." + TableColumns.ID
                + " WHERE " + TableColumns.DeliveryDate + " ='" + day + "' AND (" + TableColumns.IsDeleted + " ='0' OR " + TableColumns.DeletedOn
                + " >'" + day + "')";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }



    //Get totaldelivery for a customer to generate bill
    private double getTotalDeliveryTillDayforCustomer(String date, int id) {
        double qty = 0, adjustQty = 0;
        CustomersSettingService settingService = new CustomersSettingService();
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.DELIVERY)) {
            qty += getQuantityOfDayByDateById(date, id);
        }
        if (db.isTableNotEmpty(TableNames.CustomerSetting))
            if (custIds != null && custIds.size() > 0)
                for (int i = 0; i < custIds.size(); ++i)
                    adjustQty += settingService.getQuantityById(db.getReadableDatabase(), date, custIds.get(i));
        qty += settingService.getQuantityById(db.getReadableDatabase(), date, id) - adjustQty;


        return qty;
    }

    private double getQuantityOfDayByDateById(String day, int id) {
        String selectquery = "";
        selectquery = "SELECT * FROM " + TableNames.DELIVERY + " INNER JOIN " + TableNames.CUSTOMER
                + " ON " + TableNames.DELIVERY + "." + TableColumns.CustomerId + " =" + TableNames.CUSTOMER + "." + TableColumns.ID
                + " WHERE " + TableColumns.DeliveryDate + " ='" + day + "' AND (" + TableColumns.IsDeleted + " ='0' OR " + TableColumns.DeletedOn +
                " >'" + day + "')" + " AND " + TableColumns.CustomerId + " ='"
                + id + "'";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }*/
}
