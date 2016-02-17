package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.Constants;
import com.tyczj.extendedcalendarview.DateQuantityModel;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

import java.util.ArrayList;

/**
 * Created by Neha on 11/30/2015.
 */
public class CustomersTableMagagement {


    public static void insertCustomerDetail(SQLiteDatabase db, ExtcalVCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, holder.getAddress1());
        values.put(TableColumns.ADDRESS_2, holder.getAddress2());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.DATE_QUANTITY_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        db.insert(TableNames.TABLE_CUSTOMER, null, values);
    }

    public static void updateBalance(SQLiteDatabase db, String balance, String custId, String balanceType) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.BALANCE, balance);
        values.put(TableColumns.BALANCE_TYPE, balanceType);

        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static ArrayList<String> getCustomerId(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<String> holder = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.add(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));

            }


            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }

    public static ArrayList<ExtcalVCustomersList> getAllCustomers(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='1'";
        ArrayList<ExtcalVCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                ExtcalVCustomersList holder = new ExtcalVCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)) != null)
                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)) != null)
                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)) != null)
                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    public static ArrayList<String> getAllCustomersIds(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                list.add(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static String getAccountId(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        String id = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                id = cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return id;
    }

    public static ArrayList<String> getDates(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                list.add(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static ArrayList<ExtcalVCustomersList> getAllCustomersByArea(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='" + "1'" +
                " AND " + TableColumns.AREA_ID + " ='" + areaId + "'";
        ArrayList<ExtcalVCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                ExtcalVCustomersList holder = new ExtcalVCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)) != null)
                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)) != null)
                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)) != null)
                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }


    public static ArrayList<ExtcalVCustomersList> getAllCustomersBySelectedDate(SQLiteDatabase db, String areaid) {
        String selectquery = "";
        if (areaid.equals("")) {
            if (isDeletedCustomer(db)) {
                selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.START_DATE
                        + " <='" + Constants.DELIVERY_DATE + "'";
            } else
                selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.START_DATE
                        + " <='" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.DELETED_ON + " >='" + Constants.DELIVERY_DATE + "'";
        } else if (isDeletedCustomer(db)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.START_DATE
                    + " <='" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'";
        } else
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.START_DATE
                    + " <='" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
                    + " AND " + TableColumns.DELETED_ON + " >='" + Constants.DELIVERY_DATE + "'";


        ArrayList<ExtcalVCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                ExtcalVCustomersList holder = new ExtcalVCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)) != null)
                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)) != null)
                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)) != null)
                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
                list.add(holder);
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static String getFirstName(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        String name = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    name = cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME));


            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return name;
    }

    public static String getLastName(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'";

        String name = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    name = cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME));


            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return name;
    }

    public static ExtcalVCustomersList getAllCustomersByCustId(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='" + "1'" +
                " AND " + TableColumns.CUSTOMER_ID + " ='" + areaId + "'";
        ExtcalVCustomersList holder = new ExtcalVCustomersList();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)) != null)
                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)) != null)
                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)) != null)
                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, ExtcalVCustomersList holder, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, holder.getAddress1());
        values.put(TableColumns.ADDRESS_2, holder.getAddress2());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static void updatedeletedCustomerDetail(SQLiteDatabase db, String custId, String deletedDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, deletedDate);
        values.put(TableColumns.SYNC_STATUS, "0");
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static boolean isDeletedCustomer(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='"
                + "1'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static boolean isAreaAssociated(SQLiteDatabase db, final String areaId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.AREA_ID + " ='"
                + areaId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }


    public static ArrayList<ExtcalVCustomersList> getAllCustomersToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'";
        ArrayList<ExtcalVCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                ExtcalVCustomersList holder = new ExtcalVCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)) != null)
                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)) != null)
                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)) != null)
                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }


    public static int getTotalMilkQuantytyByDay(SQLiteDatabase db, String date) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DATE_QUANTITY_MODIFIED + " ='" + date + "'";
        int quantityTotal = 0;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    quantityTotal = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY))) + quantityTotal;
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityTotal;

    }

    public static float getTotalMilkQuantyty(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        float quantityTotal = 0;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    quantityTotal = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY))) + quantityTotal;

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityTotal;

    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }

    public static float getTotalMilkQuantytyForCustomer(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        float quantityTotal = 0;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    quantityTotal = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityTotal;

    }

    public static ArrayList<DateQuantityModel> getQuantityOfDay(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<DateQuantityModel> quantityList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();
                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                holder.setDate("");
                quantityList.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantityList;
    }

    public static String getBalanceForCustomer(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        String balance = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    balance = cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE));


            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return balance;
    }

    public static String getBalanceType(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        String balance = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();


                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
                    balance = cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE));


            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return balance;
    }

    public static ArrayList<DateQuantityModel> getAllCustomersandQuantity(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<DateQuantityModel> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                DateQuantityModel holder = new DateQuantityModel();


                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static ArrayList<String> getAllCustomersMobileNo(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

}
