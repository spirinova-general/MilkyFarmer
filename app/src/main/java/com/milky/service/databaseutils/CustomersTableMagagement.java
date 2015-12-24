package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;
import com.tyczj.extendedcalendarview.DateQuantityModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neha on 11/30/2015.
 */
public class CustomersTableMagagement {
    /*
//    * Insert Customer's details into table*/
//    public static void insertGlobaleDeliveryData(SQLiteDatabase db, JSONArray data) {
//        String sql = String.format("INSERT OR REPLACE INTO %s VALUES (?,?,?,?,?,?,?,?,?);", TableNames.TABLE_CUSTOMER);
//        SQLiteStatement statement = db.compileStatement(sql);
//        db.beginTransaction();
//        int count = data.length();
//        try {
//            for (int i = 0; i < count; ++i) {
//                JSONObject record = data.getJSONObject(i);
//                statement.clearBindings();
//                statement.bindString(1, record.getString(""));
//                statement.bindString(2, record.getString(""));
//                statement.bindString(3, record.getString(""));
//                statement.bindString(4, record.getString(""));
//                statement.bindString(5, record.getString(""));
//                statement.bindString(6, record.getString(""));
//                statement.bindString(7, record.getString(""));
//                statement.bindString(8, record.getString(""));
//                statement.bindString(9, record.getString(""));
//                statement.execute();
//            }
//        } catch (JSONException ignored) {
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//        if (db.isOpen())
//            db.close();
//
//
//    }

    public static void insertCustomerDetail(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, holder.getAddress1());
        values.put(TableColumns.ADDRESS_2, holder.getAddress2());
        values.put(TableColumns.CITY_ID, holder.getCityId());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.DELIVERY_DATE, holder.getDeliverydate());
        values.put(TableColumns.DELIVERY_DATE, holder.getDeliverydate());
        values.put(TableColumns.DATE_QUANTITY_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.ISDELETED, "0");
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");
        db.insert(TableNames.TABLE_CUSTOMER, null, values);
    }

    public static VCustomersList getCustomerById(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.CUSTOMER_ID +
                " ='" + custId + "'";
        VCustomersList holder = new VCustomersList();

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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliverydate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DIRTY)) != null)
                    holder.setDirty(cursor.getString(cursor.getColumnIndex(TableColumns.DIRTY)));
            }


            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }

    public static ArrayList<VCustomersList> getAllCustomers(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DIRTY + " ='" + "0'";
        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliverydate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
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

    public static ArrayList<VCustomersList> getAllCustomersByArea(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.ISDELETED + " ='" + "0'" +
                " AND " + TableColumns.AREA_ID + " ='" + areaId + "'";
        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliverydate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static ArrayList<VCustomersList> getAllCustomersBySelectedDate(SQLiteDatabase db) {
        Calendar c = Calendar.getInstance();
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELIVERY_DATE
                + " <='"+  Constants.DELIVERY_DATE +  "'";


        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)) != null)
                    holder.setDeliverydate(cursor.getString(cursor.getColumnIndex(TableColumns.DELIVERY_DATE)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));
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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)) != null)
                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.ISDELETED)));
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return list;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, VCustomersList holder, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, holder.getAddress1());
        values.put(TableColumns.ADDRESS_2, holder.getAddress2());
        values.put(TableColumns.CITY_ID, holder.getCityId());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.ISDELETED, "0");
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static void updatedeletedCustomerDetail(SQLiteDatabase db, String custId, String deletedDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ISDELETED, "1");
        values.put(TableColumns.ISDELETED, deletedDate);
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
    }

    public static void updateQuantity(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.DATE_QUANTITY_MODIFIED, String.valueOf(c.getTime()));
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'", null);
    }

//    public static void deleteCustomer(SQLiteDatabase db, String custId) {
//        db.delete(TableNames.TABLE_CUSTOMER, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
//    }

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
                DateQuantityModel holder = new DateQuantityModel();


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


}
