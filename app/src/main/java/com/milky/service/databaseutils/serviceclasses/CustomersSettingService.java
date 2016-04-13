package com.milky.service.databaseutils.serviceclasses;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.CustomersSetting;
import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceinterface.ICustomersSettings;
import com.milky.utils.AppUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomersSettingService implements ICustomersSettings {
    @Override
    public void insert(CustomersSetting customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, customers.getCustomerId());
        values.put(TableColumns.DefaultRate, customers.getDefaultRate());
        values.put(TableColumns.DefaultQuantity, customers.getGetDefaultQuantity());
        values.put(TableColumns.StartDate, customers.getStartDate());
        values.put(TableColumns.EndDate, customers.getEndDate());
        values.put(TableColumns.IsCustomDelivery, customers.getIsCustomDelivery());
        values.put(TableColumns.Dirty, customers.getDirty());
        values.put(TableColumns.IsDeleted,customers.getIsDeleted());
        values.put(TableColumns.DateModified,customers.getDateModified());
        getDb().insert(TableNames.CustomerSetting, null, values);
    }

    @Override
    public void update(CustomersSetting setting) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, setting.getCustomerId());
        values.put(TableColumns.DefaultRate, setting.getDefaultRate());
        values.put(TableColumns.DefaultQuantity, setting.getGetDefaultQuantity());
        values.put(TableColumns.StartDate, setting.getStartDate());
        values.put(TableColumns.EndDate, setting.getEndDate());
        values.put(TableColumns.IsCustomDelivery, setting.getIsCustomDelivery());
        values.put(TableColumns.Dirty, setting.getDirty());
        values.put(TableColumns.IsDeleted, setting.getIsDeleted());
        values.put(TableColumns.DateModified, setting.getDateModified());
        getDb().update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + setting.getCustomerId() + "'"
                + " AND " + TableColumns.StartDate + " <='" + setting.getStartDate() + "'" + " AND " +
                TableColumns.EndDate + " >='" + setting.getEndDate() + "'", null);

    }

    @Override
    public void delete(CustomersSetting setting) {
        getDb().delete(TableNames.CustomerSetting, TableColumns.ID + " ='" + setting.getId() + "'", null);
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    /*public void updateEndDate(CustomersSetting holder, String enddate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.EndDate, holder.getEndDate());

        getDb().update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.EndDate + " ='" + enddate + "'", null);
    }



    public void updateEndDateForRoll(String enddate, int customerId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.EndDate, enddate);

        getDb().update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + customerId + "'"
                + " AND " + TableColumns.StartDate + " <='" + enddate + "'" + " AND " + TableColumns.EndDate + " >'" + enddate + "'", null);
    }
    @Override
    public boolean isHasDataForDay(String date) {
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.StartDate + " <='" + date + "'";
        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    @Override
    public List<CustomersSetting> getByDate(String day) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate + " <='"
                + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'";
        ArrayList<CustomersSetting> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setDirty(1);
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setIsCustomDelivery(Utils.GetBoolean(cursor.getInt(cursor.getColumnIndex(TableColumns.IsCustomDelivery))));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    @Override
    public List<CustomersSetting> getListByCustId(int custId, String day) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate + " <='"
                + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "' AND " + TableColumns.CustomerId + " ='" + custId + "'";
        ArrayList<CustomersSetting> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    @Override
    public CustomersSetting getByCustId(int custId, String day) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate + " <='"
                + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "' AND " + TableColumns.CustomerId + " ='" + custId + "'";
        CustomersSetting holder = null;
        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
            }
            while (cursor.moveToNext());
        }
        return holder;
    }

    @Override
    public boolean isHasDataForCustoner(String day, int id) {
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.StartDate + " ='" + day + "' AND " + TableColumns.CustomerId + " ='" + id + "'";
        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    @Override
    public String getEndDate(int id, String date) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting +
                " WHERE " + TableColumns.CustomerId + " ='" + id + "' AND "
                + TableColumns.StartDate + " <='" + date + "'" + " AND " +
                TableColumns.EndDate + " >'" + date + "'";
        String enddate = "";
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {

                enddate = cursor.getString(cursor.getColumnIndex(TableColumns.EndDate));

            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return enddate;
    }



    @Override
    public List<CustomersSetting> getCustomersByArea(int id, String day) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate + " <='"
                + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "' AND " + TableColumns.AreaId + " ='" + id + "'";
        ArrayList<CustomersSetting> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
                list.add(holder);
            }
            while (cursor.moveToNext());
        }

        return list;
    }






    /*public static double getQuantityById(SQLiteDatabase db, String day, int id) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " INNER JOIN " + TableNames.CUSTOMER + " ON "
                + TableNames.CustomerSetting + "." + TableColumns.CustomerId + " =" + TableNames.CUSTOMER + "." + TableColumns.ID
                + " WHERE " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'"
                + " AND " + TableColumns.CustomerId + " ='" + id + "'";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return qty;
    }

    public static double getRateById(SQLiteDatabase db, String day, int id) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " INNER JOIN " + TableNames.CUSTOMER + " ON "
                + TableNames.CustomerSetting + "." + TableColumns.CustomerId + " =" + TableNames.CUSTOMER + "." + TableColumns.ID
                + " WHERE " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'"
                + " AND " + TableColumns.CustomerId + " ='" + id + "'";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)));
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return qty;
    }

    public static double getTotalQuantity(SQLiteDatabase db, String day) {

        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " INNER JOIN " + TableNames.CUSTOMER +
                " ON " + TableNames.CustomerSetting + "." + TableColumns.CustomerId + " =" + TableNames.CUSTOMER + "." + TableColumns.ID
                + " WHERE " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'" + " AND " +
                " (" + TableColumns.IsDeleted + " ='0'" + " OR " + TableColumns.DeletedOn + " >'" + day + "')";

        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty += cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity));

            }
            while (cursor.moveToNext());

        }


        cursor.close();

        return qty;
    }*/


}
