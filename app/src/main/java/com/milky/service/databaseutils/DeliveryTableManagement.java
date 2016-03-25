package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;


import com.milky.viewmodel.VCustomers;

import java.util.ArrayList;

/**
 * Created by Neha on 12/4/2015.
 */
public class DeliveryTableManagement {
    private static String TableNames = "delivery";

    public static void insertCustomerDetail(SQLiteDatabase db, VCustomers holder, String accountId) {

        ContentValues values = new ContentValues();
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.SERVER_ACCOUNT_ID, accountId);
        values.put(TableColumns.ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        db.insert(TableNames, null, values);
    }


    public static boolean isHasData(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='"
                + deliveryDate + "'" + " AND "
                + TableColumns.ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static String getQuantityBySelectedDay(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectQuery = "", quantity = "";
        if (isDeletedCustomer(db, deliveryDate))
            selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.DELETED_ON + " ='1'";
        else
            selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.ID + " ='" + custId + "'" + " AND "
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

        return quantity;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, VCustomers holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        long i = db.update(TableNames, values, TableColumns.START_DATE + " ='" + holder.getStart_date() + "'"
                + " AND " + TableColumns.ID +
                " ='" + holder.getCustomerId() + "'", null);
    }


    public static void updateDeletedCustomer(SQLiteDatabase db, String deletedon, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, deletedon);
        db.update(TableNames, values, TableColumns.ID +
                " ='" + custId + "'", null);
    }



    public static ArrayList<String> custIds;

    public static double getQuantityOfDayByDate(SQLiteDatabase db, String day) {
        String selectquery = "";
        selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='" + day + "' AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + day + "')";
        custIds = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }


    public static boolean isDeletedCustomer(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.DELETED_ON + " ='"
                + "1" + "' AND " + TableColumns.START_DATE + " ='" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static boolean isFromDelivery = false;

    public static double getQuantityOfDayByDateForCustomer(SQLiteDatabase db, String day, String CustId) {
        String selectquery = "SELECT * FROM " + TableNames + " WHERE " + TableColumns.START_DATE + " ='" + day + "'"
                + " AND (" + TableColumns.DELETED_ON + " ='1' OR " + TableColumns.DELETED_ON + " >'" + day + "' )" + " AND " + TableColumns.ID + " ='" + CustId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        isFromDelivery = false;
        custIds = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                isFromDelivery = true;
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return quantity;
    }

}
