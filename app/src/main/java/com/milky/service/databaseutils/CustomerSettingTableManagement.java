package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        long i = db.insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);

    }

    public static ArrayList<VCustomersList> getAllCustomersBySelectedDate(SQLiteDatabase db, String areaid) {
        String selectquery = "";
        if (areaid.equals("")) {
            if (isDeletedCustomer(db,Constants.DELIVERY_DATE)) {
                selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                        + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'"
                        + " AND " + TableColumns.DELETED_ON + " ='1'";
            } else
                selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                        + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "'";
        } else if (isDeletedCustomer(db,Constants.DELIVERY_DATE)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
                    + " AND " + TableColumns.DELETED_ON + " ='1'";
        } else
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
                    + " AND " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "'";


        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();

                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));


                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
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
        String selectquery = "";
        if (isDeletedCustomer(db, id,day)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'" + " AND "
                    + TableColumns.CUSTOMER_ID + " ='" + id + "'" + " AND " + TableColumns.DELETED_ON + " ='1'";
        } else
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'" + " AND "
                    + TableColumns.CUSTOMER_ID + " ='" + id + "'" + " AND " + TableColumns.DELETED_ON + " >'" + day + "'";

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
        String selectquery = "";
        if (isDeletedCustomer(db,day)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
            + " AND " + TableColumns.DELETED_ON + " ='1'"
            ;
        } else
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
                    + " AND " + TableColumns.DELETED_ON + " >'" + day + "'";
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
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + cId + "' AND "
                + TableColumns.START_DATE + " <='" + date + "'" + " AND " +
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

    public static boolean isDeletedCustomer(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.DELETED_ON + " ="
                + "'1'"+" AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static void updateEndDate(SQLiteDatabase db, VCustomersList holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.END_DATE, updatedEndDate);

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
        values.put(TableColumns.AREA_ID, holder.getAreaId());

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStart_date() + "'", null);
    }

    public static void updateDeletetdCustomer(SQLiteDatabase db, String custId, String deletedDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, deletedDate);
//        values.put(TableColumns.END_DATE, Constants.getCurrentDate());
        values.put(TableColumns.DATE_MODIFIED, Constants.getCurrentDate());

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static boolean isHasStartDate(SQLiteDatabase db, String custId, String startDate) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE + " >='"
                + startDate + "'" + " AND "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static boolean isDeletedCustomer(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.DELETED_ON + " ='"
                + "1" + "'" +" AND "+ TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static VCustomersList getBill(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId
                + " AND " + TableColumns.START_DATE + " <=" + deliveryDate + "' AND( " + TableColumns.END_DATE + " ='0' OR " +
                TableColumns.END_DATE + " >'" + deliveryDate + "')";
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



