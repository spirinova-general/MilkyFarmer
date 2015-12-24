package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;
import com.tyczj.extendedcalendarview.DateQuantityModel;
import com.tyczj.extendedcalendarview.Day;

import java.util.ArrayList;

/**
 * Created by Neha on 12/4/2015.
 */
public class DeliveryTbleManagement {
    public static void insertCustomerDetail(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();

        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.ACCOUNT_ID, Constants.ACCOUNT_ID);
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.DAY, Constants.QUANTITY_UPDATED_DAY);
        values.put(TableColumns.MONTH, Constants.QUANTITY_UPDATED_MONTH);
        values.put(TableColumns.YEAR, Constants.QUANTITY_UPDATED_YEAR);
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");
        db.insert(TableNames.TABLE_DELIVERY, null, values);
    }


    public static boolean isHasData(String day, String month, String year, SQLiteDatabase db, String custId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DAY + " ='"
                + day + "'" + " AND " +
                TableColumns.MONTH + " ='"
                + month + "'" + " AND " +
                TableColumns.YEAR + " ='"
                + year + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static String getCustomersBySelectedDay(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DAY
                + " ='" + Constants.QUANTITY_UPDATED_DAY + "'" +" AND "
                + TableColumns.MONTH
                + " ='" + Constants.QUANTITY_UPDATED_MONTH + "'"+" AND "
                + TableColumns.YEAR
                + " ='" + Constants.QUANTITY_UPDATED_YEAR + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        String quantity = "";

        Cursor cursor = db.rawQuery(selectquery, null);

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
        values.put(TableColumns.DAY, Constants.QUANTITY_UPDATED_DAY);
        values.put(TableColumns.MONTH, Constants.QUANTITY_UPDATED_MONTH);
        values.put(TableColumns.YEAR, Constants.QUANTITY_UPDATED_YEAR);

        db.update(TableNames.TABLE_DELIVERY, values, TableColumns.DAY + " ='" + Constants.QUANTITY_UPDATED_DAY + "'"
                + " AND " + TableColumns.MONTH + " ='" + Constants.QUANTITY_UPDATED_MONTH + "'"
                + " AND " + TableColumns.YEAR + " ='" + Constants.QUANTITY_UPDATED_YEAR + "'" +" AND "+ TableColumns.CUSTOMER_ID+
               " ='"+ holder.getCustomerId()+"'", null);
    }


    public static ArrayList<DateQuantityModel> getQuantityOfDay(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY ;
        ArrayList<DateQuantityModel> quantityList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();
                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                holder.setDay(cursor.getString(cursor.getColumnIndex(TableColumns.DAY)));
                holder.setMonth(cursor.getString(cursor.getColumnIndex(TableColumns.MONTH)));
                holder.setYear(cursor.getString(cursor.getColumnIndex(TableColumns.YEAR)));
                quantityList.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityList;
    }


}
