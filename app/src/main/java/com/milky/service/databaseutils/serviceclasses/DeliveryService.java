package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.IDelivery;
import com.milky.utils.AppUtil;
import com.milky.viewmodel.VDelivery;

import java.util.ArrayList;
import java.util.List;

public class DeliveryService implements IDelivery {
    @Override
    public void insert(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultQuantity, delivery.getQuantity());
        values.put(TableColumns.CustomerId, delivery.getCustomerId());
        values.put(TableColumns.DeliveryDate, delivery.getDeliveryDate());
        values.put(TableColumns.Dirty, 0);
        values.put(TableColumns.DateModified, delivery.getDateModified());
        getDb().insert(TableNames.DELIVERY, null, values);
    }

    @Override
    public void update(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultQuantity, delivery.getQuantity());
        values.put(TableColumns.CustomerId, delivery.getCustomerId());
        values.put(TableColumns.DeliveryDate, delivery.getDeliveryDate());
        values.put(TableColumns.Dirty, 1);
        values.put(TableColumns.DateModified, delivery.getDateModified());
        getDb().update(TableNames.DELIVERY, values, TableColumns.CustomerId + " ='" + delivery.getCustomerId() + "'" +
                " AND " + TableColumns.DeliveryDate + " ='" + delivery.getDeliveryDate() + "'", null);
    }

    @Override
    public boolean isHasDataForDay(String day, int custId) {
        String selectQuery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate + " ='"
                + day + "'" + " AND "
                + TableColumns.CustomerId + " ='" + custId + "'";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    @Override
    public void updateByDayandId(Delivery delivery, String day, int id) {

    }

    @Override
    public double getTotalDeliveryByDay(int id, String day) {
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

    public static int selectedCustomer = 0;

    @Override
    public List<Double> getTotalDelivery(int startDate, int maxDay, int month, int year, boolean isForCustomers) {
        List<Double> data = new ArrayList<>();
        for (int i = startDate; i <= maxDay; ++i) {
            if (!isForCustomers) {
                data.add(getTotalDeliveriesForMonth(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i)));
            } else {
                data.add(calculateDeliveryForCustomers(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i), selectedCustomer));
            }
        }
        return data;
    }

    @Override
    public List<VDelivery> getCustomersDelivery(String date) {
        List<VDelivery> deliveryList = new ArrayList<>();
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE (" + TableColumns.IsDeleted + " ='0'" + " OR " + TableColumns.DeletedOn + " >'" + date + "')";

//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE (" + TableColumns.DeletedOn + " ='1'" + " OR " + TableColumns.DeletedOn + " >'" + date + "')";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                VDelivery holder = new VDelivery();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setQuantity(calculateDeliveryForCustomers(date, holder.getCustomerId()));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
                holder.setFirstname(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                holder.setLastname(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                deliveryList.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        return deliveryList;
    }

    @Override
    public List<VDelivery> getByAreaAndDay(int areaId, String day) {

        List<VDelivery> deliveryList = new ArrayList<>();
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " INNER JOIN " + TableNames.CustomerSetting + " ON " + TableNames.CUSTOMER + "." + TableColumns.ID + " =" + TableNames.CustomerSetting + "." + TableColumns.CustomerId
                + " WHERE " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'"
                + " AND (" + TableColumns.IsDeleted + " ='0'" + " OR " + TableColumns.DeletedOn + " >'" + day + "') AND "
                + TableColumns.AreaId + " ='" + areaId + "'";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                VDelivery holder = new VDelivery();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setQuantity(calculateDeliveryForCustomers(day, holder.getCustomerId()));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
                holder.setFirstname(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                holder.setLastname(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                deliveryList.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        return deliveryList;

    }

    public static List<Integer> custIds;

    public double getTotalDeliveriesForMonth(String date) {
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

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    public double getQuantityOfDayByDate(String day) {
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

    //Get totaldelivery for a customer to generate bill
    public double getTotalDeliveryTillDayforCustomer(String date, int id) {
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

    public double getQuantityOfDayByDateById(String day, int id) {
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
    }
}
