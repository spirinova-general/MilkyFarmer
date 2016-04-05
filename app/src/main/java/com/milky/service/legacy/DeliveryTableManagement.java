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
        values.put(TableColumns.DefaultQuantity, holder.getGetDefaultQuantity());
        values.put(TableColumns.CustomerId, holder.getCustomerId());
        values.put(TableColumns.DeliveryDate, holder.getStartDate());
        values.put(TableColumns.Dirty, 1);
//        values.put(TableColumns.DateModified, holder.getDateModified());
        long i =db.insert(TableNames.DELIVERY, null, values);
    }


    public static boolean isHasData(SQLiteDatabase db, int custId, String deliveryDate) {
        String selectQuery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate + " ='"
                + deliveryDate + "'" + " AND "
                + TableColumns.CustomerId + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static double getQuantityBySelectedDay(SQLiteDatabase db, int custId, String deliveryDate) {
        String selectQuery = "";
        double quantity =0;
        String selectquery = "SELECT * FROM "+TableNames.CUSTOMER +" customer INNER JOIN "+TableNames.DELIVERY +
                " customer_delivery ON  customer."+TableColumns.ID+" ='customer_setting."+TableColumns.CustomerId
                +"' WHERE "+ TableColumns.DeliveryDate
                + " ='" + deliveryDate + "'" + " AND " + TableColumns.CustomerId + " ='" + custId + "'"
                + " AND (" + TableColumns.DeletedOn + " ='1'" + " OR " + TableColumns.DeletedOn + " >'" + deliveryDate + "')";

//        if (CustomersTableMagagement.isDeletedCustomer(db, deliveryDate,custId))
            selectQuery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate
                    + " ='" + deliveryDate + "'" + " AND " + TableColumns.CustomerId + " ='" + custId + "'" ;



        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                quantity = cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return quantity;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, CustomersSetting holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultQuantity, holder.getGetDefaultQuantity());
        long i = db.update(TableNames.DELIVERY, values, TableColumns.DeliveryDate + " ='" + holder.getStartDate() + "'"
                + " AND " + TableColumns.CustomerId +
                " ='" + holder.getCustomerId() + "'", null);
    }


    public static void updateDeletedCustomer(SQLiteDatabase db, String deletedon, String custId) {
        ContentValues values = new ContentValues();
        db.update(TableNames.DELIVERY, values, TableColumns.CustomerId +
                " ='" + custId + "'", null);
    }


    public static ArrayList<Integer> custIds;

    public static double getQuantityOfDayByDate(SQLiteDatabase db, String day) {
        String selectquery = "";
        selectquery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate + " ='" + day + "'";
        custIds = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
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



    public static boolean isFromDelivery = false;

    public static double getQuantityOfDayByDateForCustomer(SQLiteDatabase db, String day, int CustId) {
        String selectquery = "SELECT * FROM " + TableNames.DELIVERY + " WHERE " + TableColumns.DeliveryDate + " ='" + day + "'"
                 + " AND " + TableColumns.CustomerId + " ='" + CustId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double quantity = 0;
        isFromDelivery = false;
        custIds = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                custIds.add(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                quantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                isFromDelivery = true;
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return quantity;
    }

}
