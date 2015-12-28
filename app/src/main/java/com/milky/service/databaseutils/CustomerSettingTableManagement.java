package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;

import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;
import com.tyczj.extendedcalendarview.DateQuantityModel;

import java.util.ArrayList;

/**
 * Created by Neha on 12/11/2015.
 */
public class CustomerSettingTableManagement {

    public static void insertCustomersSetting(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.START_DATE, holder.getDateAdded());
        values.put(TableColumns.DELIVERY_DATE, holder.getDeliverydate());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.ISDELETED, "0");
        long i = db.insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);

    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }

    public static ArrayList<VCustomersList> getAllCustomersByCustomerId(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'";
        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();

                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliverydate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                list.add(holder);

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static void updateAdjustments(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ADJUSTMENTS, holder.getRate());
        values.put(TableColumns.SYNC_STATUS, "0");
        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                , null);
    }

    public static ArrayList<VCustomersList> getAllCustomersByCustomerIdToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.SYNC_STATUS + " ='"
                + "0'";
        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();

                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliverydate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                list.add(holder);

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static ArrayList<DateQuantityModel> getAllCustomers(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS;
        ArrayList<DateQuantityModel> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();


                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliveryDate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
                list.add(holder);

            }
            while (cursor.moveToNext());


        }


        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static double getAllCustomersByCustId(SQLiteDatabase db, String day, String id) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.DELIVERY_DATE + " <='" + day + "'"
                + " AND " + TableColumns.START_DATE + " <'" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + id + "'";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));


            }
            while (cursor.moveToNext());


        }


        cursor.close();
        if (db.isOpen())
            db.close();
        return qty;
    }

    public static double getAllCustomersByDay(SQLiteDatabase db, String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.DELIVERY_DATE + " <='" + day + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));


            }
            while (cursor.moveToNext());

        }


        cursor.close();
        if (db.isOpen())
            db.close();
        return qty;
    }

    public static String getOldEndDate(SQLiteDatabase db, String cId, String date) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + cId
                + " AND " + TableColumns.DELIVERY_DATE + " <='" + date + "'" + TableColumns.START_DATE + " <'" + date + "'" + " AND " +
                TableColumns.END_DATE + " >='" + date + "'";
        String enddate = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {


                enddate = cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE));


            }
            while (cursor.moveToNext());


        }

        cursor.close();
        if (db.isOpen())
            db.close();
        return enddate;
    }

    public static void updateQuantity(SQLiteDatabase db, VCustomersList holder,String enddate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.END_DATE, holder.getStart_date());
        values.put(TableColumns.DELIVERY_DATE, holder.getStart_date());
        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.END_DATE + " ='" + enddate + "'", null);
    }


    public static void updateQuantityByCustomerId(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.END_DATE, "0");

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                , null);
    }

    public static void updateData(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.DATE_MODIFIED, Constants.getCurrentDate());

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStart_date() + "'", null);
    }

    public static void updateDeletetdCustomer(SQLiteDatabase db, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ISDELETED, "1");
        values.put(TableColumns.END_DATE, Constants.getCurrentDate());
        values.put(TableColumns.DATE_MODIFIED, Constants.getCurrentDate());


        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.END_DATE + " ='" + "0" + "'"
                + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static boolean isHasStartDate(SQLiteDatabase db, String custId, String startDate) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE + " ='"
                + startDate + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static VCustomersList getBill(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId
                + " AND " + TableColumns.DELIVERY_DATE + " <=" + deliveryDate + "' AND( " + TableColumns.END_DATE + " ='0' OR " +
                TableColumns.END_DATE + " >='" + deliveryDate + "')";
        VCustomersList list = null;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                list = new VCustomersList();

                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    list.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)) != null)
                    list.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    list.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    list.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));


            }
            while (cursor.moveToNext());


        }

        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }
}



