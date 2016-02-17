package com.tyczj.extendedcalendarview;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

/**
 * Created by Neha on 12/4/2015.
 */
public class DeliveryTableManagement {
    private static String TableNames = "delivery";

    public static void insertCustomerDetail(SQLiteDatabase db, ExtcalVCustomersList holder, String qty,String accountId) {

        ContentValues values = new ContentValues();
        if (qty.equals(""))
            values.put(TableColumns.QUANTITY, holder.getQuantity());
        else
            values.put(TableColumns.QUANTITY, qty);
        values.put(TableColumns.ACCOUNT_ID, accountId);
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        db.insert(TableNames, null, values);
    }


    public static boolean isHasData(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='"
                + deliveryDate + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static String getQuantityBySelectedDay(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectQuery = "", quantity = "";
        if (isDeletedCustomer(db, deliveryDate))
            selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.DELETED_ON + " ='1'";
        else
            selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND "
                    + TableColumns.DELETED_ON + " >'" + deliveryDate + "'";


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

    public static void updateCustomerDetail(SQLiteDatabase db, ExtcalVCustomersList holder, String qty) {
        ContentValues values = new ContentValues();
        if (qty.equals(""))
            values.put(TableColumns.QUANTITY, holder.getQuantity());
        else
            values.put(TableColumns.QUANTITY, qty);
        values.put(TableColumns.START_DATE, holder.getStart_date());

        db.update(TableNames, values, TableColumns.START_DATE + " ='" + holder.getStart_date() + "'"
                + " AND " + TableColumns.CUSTOMER_ID +
                " ='" + holder.getCustomerId() + "'", null);
    }


    public static void updateDeletedCustomer(SQLiteDatabase db, String deletedon, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, deletedon);
        db.update(TableNames, values, TableColumns.CUSTOMER_ID +
                " ='" + custId + "'", null);
    }


    public static ArrayList<DateQuantityModel> getQuantityOfDay(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames;
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

    public  static ArrayList<String> custIds = new ArrayList<>();
    public static double getQuantityOfDayByDate(SQLiteDatabase db, String day) {
        String selectquery = "";
        if (isDeletedCustomer(db, day))
            selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='" + day + "'";
        else
            selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='" + day + "'"
                    + " AND " + TableColumns.DELETED_ON + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
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
        String selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
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

    public static boolean isDeletedCustomer(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.DELETED_ON + " ='"
                + "1" + "' AND " + TableColumns.START_DATE + " ='" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static double getQuantityOfDayByDateForCustomer(SQLiteDatabase db, String day, String CustId) {
        String selectquery = "";
        if (isDeletedCustomer(db, day))
            selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='" + day + "'"
                    + " AND " + TableColumns.CUSTOMER_ID + " ='" + CustId + "'";
        else
            selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='" + day + "'"
                    + " AND " + TableColumns.DELETED_ON + " >'" + day + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + CustId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantity;
    }

}
