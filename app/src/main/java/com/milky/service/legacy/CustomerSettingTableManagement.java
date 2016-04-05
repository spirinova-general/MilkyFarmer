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
//        values.put(TableColumns.CustomerId, holder.getCustomerId());
//        values.put(TableColumns.DefaultRate, holder.getDefaultRate());
//        values.put(TableColumns.DefaultQuantity, holder.getGetDefaultQuantity());
//        values.put(TableColumns.StartDate, holder.getStartDate());
//        values.put(TableColumns.EndDate, holder.getEndDate());
//        values.put(TableColumns.Dirty, "1");
//        db.insert(TableNames.CustomerSetting, null, values);
//    }

    //Get Start Delivery Date..
//    public static String getStartDeliveryDate(SQLiteDatabase db, int custId) {
//        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.CustomerId + " ='" + custId + "'"
//                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "' AND " + TableColumns.EndDate + " >'" + Constants.getCurrentDate() + "'";
//        String date = "";
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                date = cursor.getString(cursor.getColumnIndex(TableColumns.StartDate));
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//        return date;
//    }

    //Get milk Rate for customer.
//    public static double getRateByCustomerId(SQLiteDatabase db, int custId) {
//        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.CustomerId + " ='" + custId + "'"
//                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "' AND " + TableColumns.EndDate + " >'" + Constants.getCurrentDate() + "'";
//        double date = 0;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                date = cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate));
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
    selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate
                    + " <='" + date + "' AND " + TableColumns.EndDate + " >'" + date + "'" + " AND "+TableColumns.CustomerId +" ='"+custId+"'";


        CustomersSetting holder = null;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                    holder = new CustomersSetting();
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                    holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                    holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                    holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));

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
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate
//                    + " <='" + date + "' AND " + TableColumns.EndDate + " >'" + date + "'";
////            } else
////                selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate
////                        + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.EndDate + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.DeletedOn + " >'" + Constants.DELIVERY_DATE + "'";
//
//// else if (isDeletedCustomer(db, Constants.DELIVERY_DATE)) {
//        else
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate
//                    + " <='" + date + "' AND " + TableColumns.EndDate + " >'" + date + "'" + " AND " + TableColumns.AreaId + " ='" + areaid + "'"
//                ;
////        } else
////            selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.StartDate
////                    + " <='" + Constants.DELIVERY_DATE + "' AND " + TableColumns.EndDate + " >'" + Constants.DELIVERY_DATE + "'" + " AND " + TableColumns.AreaId + " ='" + areaid + "'"
////                    + " AND " + TableColumns.DeletedOn + " >'" + Constants.DELIVERY_DATE + "'";
//
//
//        ArrayList<CustomersSetting> list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                String deletedOn = CustomersTableMagagement.getCustomerDeletionDate(db,cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
//                String endDate = cursor.getString(cursor.getColumnIndex(TableColumns.EndDate));
//                Calendar deletedDate = Calendar.getInstance();
//                Calendar end = Calendar.getInstance();
//                try
//                {
//                    if(deletedOn.equals("1"))
//                    { CustomersSetting holder = new CustomersSetting();
//                        holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                        holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
//                        holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
//                        holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                        holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
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
//                            holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                            holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
//                            holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
//                            holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                            holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
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
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public static boolean isStartDateisPast(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " <'" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public static boolean isHasDataForDay(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.StartDate + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public static boolean isHasDataForDayOfCust(SQLiteDatabase db, String day, int custId) {
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.StartDate + " <='" + day + "' AND " + TableColumns.CustomerId + " ='" + custId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

//    public static void insertNewCustomersSetting(SQLiteDatabase db, VBill holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
//        values.put(TableColumns.ID, holder.getCustomerId());
//        values.put(TableColumns.DefaultRate, holder.getRate());
//        values.put(TableColumns.DefaultQuantity, holder.getQuantity());
//        values.put(TableColumns.StartDate, holder.getStartDate());
//        values.put(TableColumns.Balance, holder.getBalance());
//        values.put(TableColumns.EndDate, holder.getEndDate());
//        values.put(TableColumns.Adjustment, "0");
//        values.put(TableColumns.FirstName, holder.getFirstname());
//        values.put(TableColumns.LastName, holder.getLastName());
//        values.put(TableColumns.Dirty, "1");
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
//        values.put(TableColumns.SYNC_STATUS, "1");
//        values.put(TableColumns.DeletedOn, "1");
//        values.put(TableColumns.AreaId, getAreaId(db, holder.getCustomerId()));
//
//        long i = db.insert(TableNames.CustomerSetting, null, values);
//
//    }

//    public static String getAreaId(SQLiteDatabase db, String custId) {
//        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.ID + " ='" + custId + "'";
//        String areaId = "";
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.AreaId)) != null)
//                    areaId = cursor.getString(cursor.getColumnIndex(TableColumns.AreaId));
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
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.ID + " ='" + custId + "'";
        String areaId = "";
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)) != null)
                    areaId = cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate));
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return areaId;

    }


    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Dirty, "1");
//        values.put(TableColumns.SYNC_STATUS, "1");

//        db.update(TableNames.CustomerSetting, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
//                + " AND " + TableColumns.Dirty + " ='" + "0" + "'", null);
    }

    public static void updateBalance(SQLiteDatabase db, String balance, String custId, String balanceType, String day) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Dirty, "1");
//        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.Balance, balance);
//        values.put(TableColumns.BALANCE_TYPE, balanceType);
        values.put(TableColumns.EndDate, day);
        long i = db.update(TableNames.CustomerSetting, values, TableColumns.ID + " ='" + custId + "' AND "
                + TableColumns.StartDate + " <='" + day + "'", null);
    }

    public static CustomersSetting getAllCustomersByCustomerId(SQLiteDatabase db, int custId,String day) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "' AND "+TableColumns.StartDate +" <='"+day+"' AND "+TableColumns.EndDate +" >'"+day+"'";

        CustomersSetting holder=null;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                holder = new CustomersSetting();

                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));
                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return holder;
    }

    public static ArrayList<CustomersSetting> getAllCustomersByCustomerIdToSync(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.SYNC_STATUS + " ='"
//                + "0'";
        ArrayList<CustomersSetting> list = new ArrayList<>();

//        Cursor cursor = db.rawQuery(selectquery, null);

//        if (cursor.moveToFirst()) {
//            do {
//                CustomersSetting holder = new CustomersSetting();
//
//                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
//                holder.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                holder.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
//                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
//                list.add(holder);
//
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();

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
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
//                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)) != null)
//                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)) != null)
//                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
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
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting;
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                list.add(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
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
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting +
//                    " WHERE " + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " <='" + date + "'";
//        } else {
//
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting +
//                    " WHERE " + TableColumns.ID + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " <='" + date + "' AND "
//                    + TableColumns.DeletedOn + " >='" + date + "'";
//
//        }
//
//        ArrayList<String> startDate = new ArrayList<>();
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
//                    startDate.add(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
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
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
//                    + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'"
//                    + " AND " + TableColumns.CustomerId + " ='" + id + "'";
//        } else
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
//                    + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'"
//                    + " AND " + TableColumns.StartDate + " >'" + deletedOn + "' AND " + TableColumns.EndDate + " >'" + deletedOn + " AND "
//                    + TableColumns.CustomerId + " ='" + id + "'";
//        double qty = 0;
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
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

        selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
                + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'"
                + " AND (" + TableColumns.DeletedOn + " ='1'" + " OR " + TableColumns.DeletedOn + " >'" + day + "')";
//        } else
//            selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
//                    + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'"
//                    + " AND " + TableColumns.DeletedOn + " >'" + day + "'";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                qty += Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultQuantity)));

            }
            while (cursor.moveToNext());

        }


        cursor.close();

        return qty;
    }

    public static double getAllCustomersById(SQLiteDatabase db, String day) {

        String selectquery = "SELECT * FROM "+TableNames.CUSTOMER +" INNER JOIN "+TableNames.CustomerSetting +
                " ON "+TableNames.CUSTOMER +"."+TableColumns.ID+" ="+TableNames.CustomerSetting +"."+TableColumns.CustomerId
                +" WHERE "+ TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'"
                + " AND (" + TableColumns.DeletedOn + " ='1'" + " OR " + TableColumns.DeletedOn + " >'" + day + "')";

//        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE "
//                + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'";
////                + " AND (" + TableColumns.DeletedOn + " ='1'" + " OR " + TableColumns.DeletedOn + " >'" + day + "')";
        double qty = 0;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
//                if (CustomersTableMagagement.isDeletedCustomer(db, day,cursor.getInt(cursor.getColumnIndex(TableColumns.ID)))) {
                    qty += cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity));
//                }

            }
            while (cursor.moveToNext());

        }


        cursor.close();

        return qty;
    }

    public static String getOldEndDate(SQLiteDatabase db, int cId, String date) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting +
                " WHERE " + TableColumns.CustomerId + " ='" + cId + "' AND "
                + TableColumns.StartDate + " <='" + date + "'" + " AND " +
                TableColumns.EndDate + " >'" + date + "'";
        String enddate = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {

                enddate = cursor.getString(cursor.getColumnIndex(TableColumns.EndDate));

            }
            while (cursor.moveToNext());


        }

        cursor.close();

        return enddate;
    }


    public static void updateEndDateByArea(SQLiteDatabase db, Customers holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DefaultQuantity, holder.getQuantity());
        values.put(TableColumns.AreaId, holder.getAreaId());
        values.put(TableColumns.EndDate, updatedEndDate);

        db.update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.EndDate + " ='" + enddate + "'", null);
    }

    public static void updateEndDate(SQLiteDatabase db, String enddate, int customerId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.EndDate, enddate);

        db.update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + customerId + "'"
                + " AND " + TableColumns.StartDate + " <='" + enddate + "'" + " AND " + TableColumns.EndDate + " >'" + enddate + "'", null);
    }

    public static void updateDeletedCustomer(SQLiteDatabase db, String updatedEndDate, String id) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DefaultQuantity, holder.getQuantity());
        values.put(TableColumns.DeletedOn, updatedEndDate);

        db.update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + id + "'"
                , null);
    }


    public static void updateAllData(SQLiteDatabase db, CustomersSetting holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, holder.getCustomerId());
        values.put(TableColumns.DefaultRate, holder.getDefaultRate());
        values.put(TableColumns.DefaultQuantity, holder.getGetDefaultQuantity());
        values.put(TableColumns.StartDate, holder.getStartDate());
        values.put(TableColumns.EndDate, holder.getEndDate());
        values.put(TableColumns.Dirty, 0);

        db.update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.StartDate + " ='" + holder.getStartDate() + "'", null);
    }

    public static void updateRate(SQLiteDatabase db, CustomersSetting holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, holder.getCustomerId());
        values.put(TableColumns.DefaultRate, holder.getDefaultRate());
        values.put(TableColumns.DefaultQuantity, holder.getGetDefaultQuantity());
        values.put(TableColumns.StartDate, holder.getStartDate());
        values.put(TableColumns.EndDate, holder.getEndDate());

        db.update(TableNames.CustomerSetting, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.StartDate + " <='" + getCurrentDate() + "' AND " + TableColumns.EndDate + " >='" + getCurrentDate() + "'", null);
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.format(cal.getTime());
        return (cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" +
                String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
    }

    public static boolean isDeletedCustomer(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.DeletedOn + " ='"
                + "1" + "'" + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static CustomersSetting getBill(SQLiteDatabase db, int custId, String deliveryDate) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting +
                " WHERE " + TableColumns.CustomerId + " ='" + custId
                + " AND " + TableColumns.StartDate + " <=" + deliveryDate + "' AND( " + TableColumns.EndDate + " ='0' OR " +
                TableColumns.EndDate + " >'" + deliveryDate + "')";
        CustomersSetting list = null;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                list = new CustomersSetting();

                list.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                list.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                list.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                list.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));


            }
            while (cursor.moveToNext());


        }

        cursor.close();

        return list;
    }
    //Get Start Delivery date
    public static String getStartDatebyCustomerId(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.CustomerId + " ='" + custId + "'";
        String startDate = "";
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
                    startDate = cursor.getString(cursor.getColumnIndex(TableColumns.StartDate));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return startDate;
    }
}



