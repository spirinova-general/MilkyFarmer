package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomers;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Neha on 11/30/2015.
 */
public class CustomersTableMagagement {

    public static void insertCustomerDetail(SQLiteDatabase db, VCustomers holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
//        values.put(TableColumns.ID, holder.getCustomerId());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, holder.getAddress1());
        values.put(TableColumns.ADDRESS_2, holder.getAddress2());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.MOBILE, holder.getMobile());
//        values.put(TableColumns.QUANTITY, holder.getQuantity());
//        values.put(TableColumns.SERVER_ACCOUNT_ID, holder.getAccountId());
//        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateAdded());
//        values.put(TableColumns.DATE_QUANTITY_MODIFIED, holder.getDateAdded());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.ISDELETED, "1");
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.DIRTY, "1");
//        values.put(TableColumns.SYNC_STATUS, "1");
        long i = db.insert(TableNames.TABLE_CUSTOMER, null, values);
    }

    public static void updateBalance(SQLiteDatabase db, double balance, int custId, int balanceType) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.BALANCE, balance);
        values.put(TableColumns.BALANCE_TYPE, balanceType);

        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.ID + " ='" + custId + "'", null);
    }

    public static ArrayList<String> getCustomerId(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        ArrayList<String> holder = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
                    holder.add(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return holder;
    }
//Get Start Delivery date

    public static String getStartDatebyCustomerId(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
        String startDate = "";
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    startDate = cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return startDate;
    }

    public static ArrayList<VCustomers> getAllCustomers(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='1'";
        ArrayList<VCustomers> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomers holder = new VCustomers();
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
//                holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)));
                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
//                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
//                holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
                holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
//                holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }

        return list;
    }


    public static String getAccountId(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER;
        String id = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                id = cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

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

        return list;
    }

    public static ArrayList<VCustomers> getAllCustomersByArea(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='" + "1'" +
                " AND " + TableColumns.AREA_ID + " ='" + areaId + "'";
        ArrayList<VCustomers> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomers holder = new VCustomers();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
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
                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
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

        return list;
    }

    public static String getFirstName(SQLiteDatabase db, final int custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE "
                + TableColumns.ID + " ='" + custId + "'";
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

        return name;
    }

    public static String getLastName(SQLiteDatabase db, final int custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE "
                + TableColumns.ID + " ='" + custId + "'";

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

        return name;
    }

    public static VCustomers getAllCustomersByCustId(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='" + "1'" +
                " AND " + TableColumns.ID + " ='" + areaId + "'";
        VCustomers holder = new VCustomers();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
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
                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
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

        return holder;
    }

    public static void updateCustomerDetail(SQLiteDatabase db, VCustomers holder, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.ID, holder.getCustomerId());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, holder.getAddress1());
        values.put(TableColumns.ADDRESS_2, holder.getAddress2());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.SERVER_ACCOUNT_ID, holder.getAccountId());
//        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateAdded());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.ID + " ='" + custId + "'", null);
    }

    public static void updatedeletedCustomerDetail(SQLiteDatabase db, String custId, String deletedDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, deletedDate);
        values.put(TableColumns.SYNC_STATUS, "0");
        db.update(TableNames.TABLE_CUSTOMER, values, TableColumns.ID + " ='" + custId + "'", null);
    }

    public static boolean isDeletedCustomer(SQLiteDatabase db, String custId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE (" + TableColumns.DELETED_ON + " ='"
                + "1' OR "+TableColumns.DELETED_ON+" >'"+ Constants.getCurrentDate()+"') AND " + TableColumns.ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static boolean isAreaAssociated(SQLiteDatabase db, final int areaId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.AREA_ID + " ='"
                + areaId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }


    public static String getCustomerDeletionDate(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
        String date = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                date = cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON));
//                try {
//                    if (!date.equals("1")) {
//                        Date d = Constants.work_format.parse(date);
//                        cal.setTime(d);
//                        date = String.valueOf(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH))
//                                + "-" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH) - 1));
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return date;
    }

    public static ArrayList<VCustomers> getAllCustomersToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'";
        ArrayList<VCustomers> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomers holder = new VCustomers();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
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
                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
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
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
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

        return quantityTotal;

    }


    public static String getBalanceForCustomer(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
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

        return balance;
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

        return list;
    }

    public static String getCustomerMobileNo(SQLiteDatabase db, String id) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.ID + " ='" + id + "'";
        String list = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                list = cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE));
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    }
