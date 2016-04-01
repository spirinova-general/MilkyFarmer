package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;

import java.util.ArrayList;

/**
 * Created by Neha on 12/4/2015.
 */
public class DeliveryTableManagement {


    public static void insertCustomerDetail(SQLiteDatabase db, CustomersSetting holder) {

        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getGetDefaultQuantity());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.DELEVERY_DATE, holder.getStartDate());
        values.put(TableColumns.DIRTY, 1);
//        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        long i =db.insert(TableNames.TABLE_DELIVERY, null, values);
    }


    public static boolean isHasData(SQLiteDatabase db, int custId, String deliveryDate) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE + " ='"
                + deliveryDate + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static double getQuantityBySelectedDay(SQLiteDatabase db, int custId, String deliveryDate) {
        String selectQuery = "";
        double quantity =0;
        String selectquery = "SELECT * FROM "+TableNames.TABLE_CUSTOMER+" customer INNER JOIN "+TableNames.TABLE_DELIVERY+
                " customer_delivery ON  customer."+TableColumns.ID+" ='customer_setting."+TableColumns.CUSTOMER_ID
                +"' WHERE "+ TableColumns.DELEVERY_DATE
                + " ='" + deliveryDate + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'"
                + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + deliveryDate + "')";

//        if (CustomersTableMagagement.isDeletedCustomer(db, deliveryDate,custId))
            selectQuery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" ;



        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                quantity = cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, CustomersSetting holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getGetDefaultQuantity());
        long i = db.update(TableNames.TABLE_DELIVERY, values, TableColumns.DELEVERY_DATE + " ='" + holder.getStartDate() + "'"
                + " AND " + TableColumns.CUSTOMER_ID +
                " ='" + holder.getCustomerId() + "'", null);
    }


    public static void updateDeletedCustomer(SQLiteDatabase db, String deletedon, String custId) {
        ContentValues values = new ContentValues();
        db.update(TableNames.TABLE_DELIVERY, values, TableColumns.CUSTOMER_ID +
                " ='" + custId + "'", null);
    }


    public static ArrayList<Integer> custIds;

    public static double getQuantityOfDayByDate(SQLiteDatabase db, String day) {
        String selectquery = "";
        selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE + " ='" + day + "'";
        custIds = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
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



    public static boolean isFromDelivery = false;

    public static double getQuantityOfDayByDateForCustomer(SQLiteDatabase db, String day, int CustId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_DELIVERY + " WHERE " + TableColumns.DELEVERY_DATE + " ='" + day + "'"
                 + " AND " + TableColumns.CUSTOMER_ID + " ='" + CustId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        isFromDelivery = false;
        custIds = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                isFromDelivery = true;
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return quantity;
    }

}
