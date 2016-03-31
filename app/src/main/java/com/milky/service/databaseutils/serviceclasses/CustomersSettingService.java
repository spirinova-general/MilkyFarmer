package com.milky.service.databaseutils.serviceclasses;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.ICustomersSetting;
import com.milky.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomersSettingService implements ICustomersSetting {
    @Override
    public void insert(CustomersSetting customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, customers.getCustomerId());
        values.put(TableColumns.DEFAULT_RATE, customers.getDefaultRate());
        values.put(TableColumns.DEFAULT_QUANTITY, customers.getGetDefaultQuantity());
        values.put(TableColumns.START_DATE, customers.getStartDate());
        values.put(TableColumns.END_DATE, customers.getEndDate());
        values.put(TableColumns.DIRTY, 1);
        getDb().insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);
    }

    @Override
    public void update(CustomersSetting customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, customers.getCustomerId());
        values.put(TableColumns.DEFAULT_RATE, customers.getDefaultRate());
        values.put(TableColumns.DEFAULT_QUANTITY, customers.getGetDefaultQuantity());
        values.put(TableColumns.START_DATE, customers.getStartDate());
        values.put(TableColumns.END_DATE, customers.getEndDate());
        values.put(TableColumns.DIRTY, 0);
        getDb().update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + customers.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " <='" + customers.getStartDate() + "'" + " AND " + TableColumns.END_DATE + " >'" + customers.getEndDate() + "'", null);
    }

    @Override
    public void delete(CustomersSetting customers) {

    }

    @Override
    public boolean isHasDataForDay(String date) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + date + "'";
        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    @Override
    public List<CustomersSetting> getByDate(String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE + " <='"
                + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";
        ArrayList<CustomersSetting> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setDirty(1);
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    @Override
    public List<CustomersSetting> getListByCustId(int custId, String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE + " <='"
                + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "' AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        ArrayList<CustomersSetting> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setDirty(1);
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    @Override
    public CustomersSetting getByCustId(int custId, String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE + " <='"
                + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "' AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        CustomersSetting holder = null;
        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                holder = new CustomersSetting();
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setDirty(1);
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
            }
            while (cursor.moveToNext());
        }
        return holder;
    }

    @Override
    public boolean isHasDataForCustoner(String day, int id) {

        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "' AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";
        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;

    }

    @Override
    public List<CustomersSetting> getCustomersByArea(int id, String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE + " <='"
                + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "' AND " + TableColumns.AREA_ID + " ='" + id + "'";
        ArrayList<CustomersSetting> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setDirty(1);
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }

        return list;
    }


    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    public static double getQuantityById(SQLiteDatabase db, String day, int id) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'"
                + " AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
            }
            while (cursor.moveToNext());
        }


        cursor.close();

        return qty;
    }

    public static double getTotalQuantity(SQLiteDatabase db, String day) {

        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " INNER JOIN " + TableNames.TABLE_CUSTOMER +
                " ON " + TableNames.TABLE_CUSTOMER_SETTINGS + "." + TableColumns.CUSTOMER_ID + " =" + TableNames.TABLE_CUSTOMER + "." + TableColumns.ID
                + " WHERE " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
                + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + day + "')";

        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty += cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY));

            }
            while (cursor.moveToNext());

        }


        cursor.close();

        return qty;
    }

}
