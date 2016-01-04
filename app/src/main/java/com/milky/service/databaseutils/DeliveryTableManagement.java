package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;
import com.tyczj.extendedcalendarview.DateQuantityModel;

import java.util.ArrayList;

/**
 * Created by Neha on 12/4/2015.
 */
public class DeliveryTableManagement {
    public static void insertCustomerDetail(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.ACCOUNT_ID, Constants.ACCOUNT_ID);
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        db.insert(TableNames.TABLE_DELIVERY, null, values);
    }


    public static boolean isHasData(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.START_DATE + " ='"
                + deliveryDate + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static String getQuantityBySelectedDay(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectQuery = "",quantity="";
        if (isDeletedCustomer(db))
            selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.START_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        else
            selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.START_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND "
                    + TableColumns.DELETED_ON + " >='" + deliveryDate + "'";


        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    quantity = cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY));

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantity;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.START_DATE, holder.getStart_date());

        db.update(TableNames.TABLE_DELIVERY, values, TableColumns.START_DATE + " ='" + holder.getStart_date() + "'"
                + " AND " + TableColumns.CUSTOMER_ID +
                " ='" + holder.getCustomerId() + "'", null);
    }

    public static void updateDeletedCustomer(SQLiteDatabase db, String deletedon, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, deletedon);
        db.update(TableNames.TABLE_DELIVERY, values, TableColumns.CUSTOMER_ID +
                " ='" + custId + "'", null);
    }


    public static ArrayList<DateQuantityModel> getQuantityOfDay(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY;
        ArrayList<DateQuantityModel> quantityList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();
                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                holder.setDeliveryDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                quantityList.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityList;
    }

    public static double getQuantityOfDayByDate(SQLiteDatabase db, String day) {
        String selectquery = "";
        if (isDeletedCustomer(db))
            selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.START_DATE + " ='" + day + "'";
        else
            selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.START_DATE + " ='" + day + "'"
                    + " AND " + TableColumns.DELETED_ON + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                AppUtil.custIds.add(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantity;
    }

    public static ArrayList<DateQuantityModel> getMilkQuantityofCustomer(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        ArrayList<DateQuantityModel> quantityList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();
                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                holder.setDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                quantityList.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityList;
    }

    public static boolean isDeletedCustomer(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.DELETED_ON + " ='"
                + "1" + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }


}
