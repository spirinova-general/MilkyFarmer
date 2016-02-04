package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TableRow;

import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomersList;
import com.tyczj.extendedcalendarview.DateQuantityModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        long i = db.insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);

    }

    public static boolean isHasDataForDayById(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return result;
    }

    public static boolean isHasDataForDay(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return result;
    }

    public static void insertNewCustomersSetting(SQLiteDatabase db, VBill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.BALANCE, holder.getBalance());
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.FIRST_NAME, holder.getFirstname());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.AREA_ID, getAreaId(db, holder.getCustomerId()));

        long i = db.insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);

    }

    public static String getAreaId(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        String areaId = "";
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    areaId = cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaId;

    }

    public static String getPrice(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        String areaId = "";
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    areaId = cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaId;

    }

    public static ArrayList<VCustomersList> getAllCustomersBySelectedDate(SQLiteDatabase db, String areaid) {
        String selectquery = "";
        if (areaid.equals("")) {
//            if (isDeletedCustomer(db, Constants.DELIVERY_DATE)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >='" + Constants.DELIVERY_DATE + "'"
                    + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "')";
//            } else
//                selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
//                        + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "'";
        }
// else if (isDeletedCustomer(db, Constants.DELIVERY_DATE)) {
        else
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >='" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
                    + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "')";
//        } else
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
//                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
//                    + " AND " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "'";


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
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
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

    public static void updateBalance(SQLiteDatabase db, String balance, String custId,String balanceType,String day) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.BALANCE, balance);
        values.put(TableColumns.BALANCE_TYPE,balanceType);
        values.put(TableColumns.END_DATE,day);
        long i =db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'", null);
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

    public static ArrayList<String> getDates(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS;
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
    public static ArrayList<String> getStartDeliveryDate(SQLiteDatabase db, String custId) {
        String selectquery = null;
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
                + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        if (isDeletedCustomerById(db, custId)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                    " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" +" AND "+TableColumns.START_DATE+" <='"+date+"'";
        } else {

            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                    " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE+" <='"+date+"' AND"
                    + TableColumns.DELETED_ON + " >='" + date + "'";

        }

        ArrayList<String> startDate = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    startDate.add(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return startDate;

    }
    public static double getAllCustomersByCustId(SQLiteDatabase db, String day, String id) {
        String selectquery = "";
        if (isDeletedCustomer(db, id, day)) {
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE +
                    " >='" + day + "'" + " AND "
                    + TableColumns.CUSTOMER_ID + " ='" + id + "'" + " AND " + TableColumns.DELETED_ON + " ='1'";
        } else
            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE
                    + " >'" + day + "'" + " AND "
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
        String selectquery = "";
//        if (isDeletedCustomer(db, day)) {
        selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'"
                + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + day + "')";
//        } else
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
//                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
//                    + " AND " + TableColumns.DELETED_ON + " >'" + day + "'";
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
                + "'1'" + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE +
                " >='" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static boolean isDeletedCustomerById(SQLiteDatabase db, String id) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.DELETED_ON + " ="
                + "'1'" + " AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";

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

    public static void updateDeletedCustomer(SQLiteDatabase db, String updatedEndDate, String id) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.DELETED_ON, updatedEndDate);

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + id + "'"
                , null);
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
//        Calendar cal = Calendar.getInstance();
//        try {
//            Date d = Constants._display_format.parse(holder.getStart_date());
//            cal.setTime(d);
//            holder.setStart_date(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH)) + "-"
//                    + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " >='" + holder.getStart_date() + "'", null);
    }

    public static void updateAllData(SQLiteDatabase db, VCustomersList holder) {
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
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.DELETED_ON, "1");
        values.put(TableColumns.AREA_ID, holder.getAreaId());


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
                + "1" + "'" + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static VCustomersList getBill(SQLiteDatabase db, String custId, String deliveryDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId
                + " AND " + TableColumns.START_DATE + " <=" + deliveryDate + "' AND( " + TableColumns.END_DATE + " ='0' OR " +
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



