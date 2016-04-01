package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Neha on 12/11/2015.
 */
public class CustomerSettingTableManagement {

//    public static void insertCustomersSetting(SQLiteDatabase db, CustomersSetting holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
//        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getGetDefaultQuantity());
//        values.put(TableColumns.START_DATE, holder.getStartDate());
//        values.put(TableColumns.END_DATE, holder.getEndDate());
//        values.put(TableColumns.DIRTY, "1");
//        db.insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);
//    }

    //Get Start Delivery Date..
//    public static String getStartDeliveryDate(SQLiteDatabase db, int custId) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'"
//                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "' AND " + TableColumns.END_DATE + " >'" + Constants.getCurrentDate() + "'";
//        String date = "";
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                date = cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE));
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//        return date;
//    }

    //Get milk Rate for customer.
//    public static double getRateByCustomerId(SQLiteDatabase db, int custId) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'"
//                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "' AND " + TableColumns.END_DATE + " >'" + Constants.getCurrentDate() + "'";
//        double date = 0;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                date = cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE));
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//        return date;
//    }


    //Get Settings data for customer..
    public static CustomersSetting getDataForCustomer(SQLiteDatabase db,int custId) {
    String selectquery = "";
    String date = Constants.getCurrentDate();
    selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
                    + " <='" + date + "' AND " + TableColumns.END_DATE + " >'" + date + "'" + " AND "+TableColumns.CUSTOMER_ID+" ='"+custId+"'";


        CustomersSetting holder = null;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                    holder = new CustomersSetting();
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                    holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                    holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                    holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return holder;
    }

//    public static ArrayList<CustomersSetting> customersForSelectedDates(SQLiteDatabase db, String areaid, String date) {
//        String selectquery;
//
//        if (areaid.equals(""))
////            if (isDeletedCustomer(db, Constants.DELIVERY_DATE)) {
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
//                    + " <='" + date + "' AND " + TableColumns.END_DATE + " >'" + date + "'";
////            } else
////                selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
////                        + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "'";
//
//// else if (isDeletedCustomer(db, Constants.DELIVERY_DATE)) {
//        else
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
//                    + " <='" + date + "' AND " + TableColumns.END_DATE + " >'" + date + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
//                ;
////        } else
////            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.START_DATE
////                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.END_DATE + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AREA_ID + " ='" + areaid + "'"
////                    + " AND " + TableColumns.DELETED_ON + " >'" + Constants.DELIVERY_DATE + "'";
//
//
//        ArrayList<CustomersSetting> list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                String deletedOn = CustomersTableMagagement.getCustomerDeletionDate(db,cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
//                String endDate = cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE));
//                Calendar deletedDate = Calendar.getInstance();
//                Calendar end = Calendar.getInstance();
//                try
//                {
//                    if(deletedOn.equals("1"))
//                    { CustomersSetting holder = new CustomersSetting();
//                        holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                        holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
//                        holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
//                        holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
//                        holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
//                        list.add(holder);
//                    }
//                    else
//                    { CustomersSetting holder = new CustomersSetting();
//                        Date delete = Constants.work_format.parse(deletedOn);
//                        Date endDateSelected = Constants.work_format.parse(endDate);
//                        end.setTime(endDateSelected);
//                        deletedDate.setTime(delete);
//                        if(deletedDate.before(end))
//                        {
//                            holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                            holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
//                            holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
//                            holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
//                            holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
//                            list.add(holder);
//                        }
//                    }
//                }
//                catch (ParseException pexp)
//                {
//
//                }
//
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return list;
//    }

    public static boolean isHasDataForDayById(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public static boolean isStartDateisPast(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " <'" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public static boolean isHasDataForDay(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public static boolean isHasDataForDayOfCust(SQLiteDatabase db, String day, int custId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "' AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

//    public static void insertNewCustomersSetting(SQLiteDatabase db, VBill holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
//        values.put(TableColumns.ID, holder.getCustomerId());
//        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
//        values.put(TableColumns.START_DATE, holder.getStartDate());
//        values.put(TableColumns.BALANCE, holder.getBalance());
//        values.put(TableColumns.END_DATE, holder.getEndDate());
//        values.put(TableColumns.ADJUSTMENTS, "0");
//        values.put(TableColumns.FIRST_NAME, holder.getFirstname());
//        values.put(TableColumns.LAST_NAME, holder.getLastName());
//        values.put(TableColumns.DIRTY, "1");
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
//        values.put(TableColumns.SYNC_STATUS, "1");
//        values.put(TableColumns.DELETED_ON, "1");
//        values.put(TableColumns.AREA_ID, getAreaId(db, holder.getCustomerId()));
//
//        long i = db.insert(TableNames.TABLE_CUSTOMER_SETTINGS, null, values);
//
//    }

//    public static String getAreaId(SQLiteDatabase db, String custId) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.ID + " ='" + custId + "'";
//        String areaId = "";
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
//                    areaId = cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID));
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//
//        return areaId;
//
//    }

    public static String getPrice(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.ID + " ='" + custId + "'";
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

        return areaId;

    }


    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }

    public static void updateBalance(SQLiteDatabase db, String balance, String custId, String balanceType, String day) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.BALANCE, balance);
        values.put(TableColumns.BALANCE_TYPE, balanceType);
        values.put(TableColumns.END_DATE, day);
        long i = db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.ID + " ='" + custId + "' AND "
                + TableColumns.START_DATE + " <='" + day + "'", null);
    }

    public static CustomersSetting getAllCustomersByCustomerId(SQLiteDatabase db, int custId,String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "' AND "+TableColumns.START_DATE+" <='"+day+"' AND "+TableColumns.END_DATE+" >'"+day+"'";

        CustomersSetting holder=null;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                holder = new CustomersSetting();

                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return holder;
    }

    public static ArrayList<CustomersSetting> getAllCustomersByCustomerIdToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.SYNC_STATUS + " ='"
                + "0'";
        ArrayList<CustomersSetting> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = new CustomersSetting();

                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                list.add(holder);

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

//    public static ArrayList<DateQuantityModel> getAllCustomers(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + "customers";
//        ArrayList<DateQuantityModel> list = new ArrayList<>();
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                DateQuantityModel holder = new DateQuantityModel();
//
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
//                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
//                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
//                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)) != null)
//                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
//                list.add(holder);
//
//            }
//            while (cursor.moveToNext());
//
//
//        }
//
//
//        cursor.close();
//
//        return list;
//    }

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

        return list;
    }

//    public static ArrayList<String> getStartDeliveryDate(SQLiteDatabase db, String custId) {
//        String selectquery = null;
//        Calendar cal = Calendar.getInstance();
//        String date = cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
//                + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
//        if (isDeletedCustomerById(db, custId)) {
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
//                    " WHERE " + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " <='" + date + "'";
//        } else {
//
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
//                    " WHERE " + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " <='" + date + "' AND "
//                    + TableColumns.DELETED_ON + " >='" + date + "'";
//
//        }
//
//        ArrayList<String> startDate = new ArrayList<>();
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
//                    startDate.add(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//        return startDate;
//    }

//    public static double getAllCustomersByCustId(SQLiteDatabase db, String day, int id) {
//        String selectquery = "";
//        String deletedOn = CustomersTableMagagement.getCustomerDeletionDate(db, id);
//
//        if ("1".equals(deletedOn)) {
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
//                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'"
//                    + " AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";
//        } else
//            selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
//                    + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'"
//                    + " AND " + TableColumns.START_DATE + " >'" + deletedOn + "' AND " + TableColumns.END_DATE + " >'" + deletedOn + " AND "
//                    + TableColumns.CUSTOMER_ID + " ='" + id + "'";
//        double qty = 0;
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
//            }
//            while (cursor.moveToNext());
//        }
//
//
//        cursor.close();
//
//        return qty;
//    }


    public static double getAllCustomersByDay(SQLiteDatabase db, String day) {
        String selectquery = "";
//        if (isDeletedCustomer(db, day)) {

        selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
                + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
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

        return qty;
    }

    public static double getAllCustomersById(SQLiteDatabase db, String day) {

        String selectquery = "SELECT * FROM "+TableNames.TABLE_CUSTOMER+" INNER JOIN "+TableNames.TABLE_CUSTOMER_SETTINGS+
                " ON "+TableNames.TABLE_CUSTOMER+"."+TableColumns.ID+" ="+TableNames.TABLE_CUSTOMER_SETTINGS+"."+TableColumns.CUSTOMER_ID
                +" WHERE "+ TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'"
                + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + day + "')";

//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE "
//                + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";
////                + " AND (" + TableColumns.DELETED_ON + " ='1'" + " OR " + TableColumns.DELETED_ON + " >'" + day + "')";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
//                if (CustomersTableMagagement.isDeletedCustomer(db, day,cursor.getInt(cursor.getColumnIndex(TableColumns.ID)))) {
                    qty += cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY));
//                }

            }
            while (cursor.moveToNext());

        }


        cursor.close();

        return qty;
    }

    public static String getOldEndDate(SQLiteDatabase db, int cId, String date) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + cId + "' AND "
                + TableColumns.START_DATE + " <='" + date + "'" + " AND " +
                TableColumns.END_DATE + " >'" + date + "'";
        String enddate = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {

                enddate = cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE));

            }
            while (cursor.moveToNext());


        }

        cursor.close();

        return enddate;
    }


    public static void updateEndDateByArea(SQLiteDatabase db, Customers holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.END_DATE, updatedEndDate);

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.END_DATE + " ='" + enddate + "'", null);
    }

    public static void updateEndDate(SQLiteDatabase db, String enddate, int customerId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.END_DATE, enddate);

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + customerId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + enddate + "'" + " AND " + TableColumns.END_DATE + " >'" + enddate + "'", null);
    }

    public static void updateDeletedCustomer(SQLiteDatabase db, String updatedEndDate, String id) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.DELETED_ON, updatedEndDate);

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + id + "'"
                , null);
    }


    public static void updateAllData(SQLiteDatabase db, CustomersSetting holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getGetDefaultQuantity());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.DIRTY, 0);

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStartDate() + "'", null);
    }

    public static void updateRate(SQLiteDatabase db, CustomersSetting holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getGetDefaultQuantity());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.END_DATE, holder.getEndDate());

        db.update(TableNames.TABLE_CUSTOMER_SETTINGS, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " <='" + getCurrentDate() + "' AND " + TableColumns.END_DATE + " >='" + getCurrentDate() + "'", null);
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.format(cal.getTime());
        return (cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
    }

    public static boolean isDeletedCustomer(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.DELETED_ON + " ='"
                + "1" + "'" + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static CustomersSetting getBill(SQLiteDatabase db, int custId, String deliveryDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS +
                " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId
                + " AND " + TableColumns.START_DATE + " <=" + deliveryDate + "' AND( " + TableColumns.END_DATE + " ='0' OR " +
                TableColumns.END_DATE + " >'" + deliveryDate + "')";
        CustomersSetting list = null;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                list = new CustomersSetting();

                list.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                list.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                list.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                list.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));


            }
            while (cursor.moveToNext());


        }

        cursor.close();

        return list;
    }
    //Get Start Delivery date
    public static String getStartDatebyCustomerId(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
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
}



