package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.IDelivery;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VDelivery;

import java.util.ArrayList;
import java.util.List;

public class DeliveryService implements IDelivery {
    @Override
    public void insert(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_QUANTITY, delivery.getQuantity());
        values.put(TableColumns.CUSTOMER_ID, delivery.getCustomerId());
        values.put(TableColumns.DELEVERY_DATE, delivery.getDeliveryDate());
        values.put(TableColumns.DIRTY, 1);
        values.put(TableColumns.DATE_MODIFIED, delivery.getDateModified());
        getDb().insert(TableNames.TABLE_DELIVERY, null, values);
    }

    @Override
    public void update(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_QUANTITY, delivery.getQuantity());
        values.put(TableColumns.CUSTOMER_ID, delivery.getCustomerId());
        values.put(TableColumns.DELEVERY_DATE, delivery.getDeliveryDate());
        values.put(TableColumns.DIRTY, 0);
        values.put(TableColumns.DATE_MODIFIED, delivery.getDateModified());
        getDb().update(TableNames.TABLE_DELIVERY, values, TableColumns.CUSTOMER_ID + " ='" + delivery.getCustomerId() + "'" +
                " AND " + TableColumns.DELEVERY_DATE + " ='" + delivery.getDeliveryDate() + "'", null);
    }

    @Override
    public boolean isHasDataForDay(String day, int custId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE + " ='"
                + day + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

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

        String selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE + " ='" + day + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";


        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                quantity = cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }

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
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " INNER JOIN " + TableNames.TABLE_CUSTOMER_SETTINGS + " ON " + TableNames.TABLE_CUSTOMER + "." + TableColumns.ID + " =" + TableNames.TABLE_CUSTOMER_SETTINGS + "." + TableColumns.CUSTOMER_ID
                + " WHERE (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + date + "') AND " + TableColumns.START_DATE + " <='" + date + "'"
                + " AND " + TableColumns.END_DATE + " >'" + date + "'";

//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + date + "')";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                VDelivery holder = new VDelivery();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setQuantity(calculateDeliveryForCustomers(date, holder.getCustomerId()));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
                holder.setFirstname(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastname(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
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
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " INNER JOIN " + TableNames.TABLE_CUSTOMER_SETTINGS + " ON " + TableNames.TABLE_CUSTOMER + "." + TableColumns.ID + " =" + TableNames.TABLE_CUSTOMER_SETTINGS + "." + TableColumns.CUSTOMER_ID
                + " WHERE " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
                + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + day + "') AND "
                + TableColumns.AREA_ID + " ='" + areaId + "'";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                VDelivery holder = new VDelivery();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setQuantity(calculateDeliveryForCustomers(day, holder.getCustomerId()));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
                holder.setFirstname(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastname(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                deliveryList.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        return deliveryList;

    }

    public static List<Integer> custIds;
    public static int selectedCustomer = 0;

    public double getTotalDeliveriesForMonth(String date) {
        double qty = 0, adjustQty = 0;
        CustomersSettingService settingService = new CustomersSettingService();
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            qty += getQuantityOfDayByDate(date);
        }
        if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS))
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
        if (db.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            if (!isHasDataForDay(date, id)) {
                if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {
                    qty = settingService.getQuantityById(db.getReadableDatabase(), date
                            , id);

                }
            } else
                qty = getTotalDeliveryByDay(id, date);


        } else if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {

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
        selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE + " ='" + day + "'";
        custIds = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }

    //Get quantity total for some dates, for bill
    public double getTotalQuantityConsumed(int startDate, int maxDay, int month, int year, boolean isForCustomers) {
        double data = 0;
        for (int i = startDate; i <= maxDay; ++i) {
            if (!isForCustomers) {
                data += getTotalDeliveriesForMonth(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i));
            } else {
                data += calculateDeliveryForCustomers(String.valueOf(year) + "-" + String.format("%02d", month + 1) +
                        "-" + String.format("%02d", i), selectedCustomer);
            }
        }
        return data;
    }

}
