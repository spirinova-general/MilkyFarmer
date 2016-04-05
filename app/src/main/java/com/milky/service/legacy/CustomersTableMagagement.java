package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;

import java.util.ArrayList;

/**
 * Created by Neha on 11/30/2015.
 */
public class CustomersTableMagagement {

    public static void insertCustomerDetail(SQLiteDatabase db, Customers holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FirstName, holder.getFirstName());
//        values.put(TableColumns.ID, holder.getCustomerId());
        values.put(TableColumns.LastName, holder.getLastName());
        values.put(TableColumns.Balance, holder.getBalance_amount());
        values.put(TableColumns.Address1, holder.getAddress1());
        values.put(TableColumns.Address2, holder.getAddress2());
        values.put(TableColumns.AreaId, holder.getAreaId());
        values.put(TableColumns.Mobile, holder.getMobile());
//        values.put(TableColumns.QUANTITY, holder.getQuantity());
//        values.put(TableColumns.ServerAccountId, holder.getAccountId());
//        values.put(TableColumns.DefaultRate, holder.getRate());
        values.put(TableColumns.DateAdded, holder.getDateAdded());
        values.put(TableColumns.DateModified, holder.getDateAdded());
//        values.put(TableColumns.DATE_QUANTITY_MODIFIED, holder.getDateAdded());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.IsDeleted, 1);
        values.put(TableColumns.DeletedOn, "1");
        values.put(TableColumns.Dirty, 1);
//        values.put(TableColumns.SYNC_STATUS, "1");
        long i = db.insert(TableNames.CUSTOMER, null, values);
    }

    public static void updateBalance(SQLiteDatabase db, double balance, int custId, int balanceType) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Balance, balance);
//        values.put(TableColumns.BALANCE_TYPE, balanceType);

        db.update(TableNames.CUSTOMER, values, TableColumns.ID + " ='" + custId + "'", null);
    }

    public static ArrayList<String> getCustomerIdList(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER;
        ArrayList<String> holder = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                    holder.add(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return holder;
    }

//    //Get CustomerId
//    public static int getCustomerId(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER;
//        int id = 0;
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                id = cursor.getInt(cursor.getColumnIndex(TableColumns.ID));
//
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//
//        return id;
//    }

    public static String getAccountId(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER;
        String id = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {


                id = cursor.getString(cursor.getColumnIndex(TableColumns.ServerAccountId));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return id;
    }

    public static ArrayList<String> getDates(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER;
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

//    public static String getFirstName(SQLiteDatabase db, final int custId) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE "
//                + TableColumns.ID + " ='" + custId + "'";
//        String name = "";
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                    name = cursor.getString(cursor.getColumnIndex(TableColumns.FirstName));
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return name;
//    }

//    public static String getLastName(SQLiteDatabase db, final int custId) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE "
//                + TableColumns.ID + " ='" + custId + "'";
//
//        String name = "";
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.LastName)) != null)
//                    name = cursor.getString(cursor.getColumnIndex(TableColumns.LastName));
//
//
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return name;
//    }

//    public static VCustomers getAllCustomersByCustId(SQLiteDatabase db, final String areaId) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.DeletedOn + " ='" + "1'" +
//                " AND " + TableColumns.ID + " ='" + areaId + "'";
//        VCustomers holder = new VCustomers();
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
//                    holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
//                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
//                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Balance)) != null)
//                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Address1)) != null)
//                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.Address1)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Address2)) != null)
//                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.Address2)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.AreaId)) != null)
//                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)) != null)
//                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)) != null)
//                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
//                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)) != null)
//                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
//                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return holder;
//    }

    public static void updateCustomerDetail(SQLiteDatabase db, Customers holder, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FirstName, holder.getFirstName());
        values.put(TableColumns.ID, holder.getCustomerId());
        values.put(TableColumns.LastName, holder.getLastName());
        values.put(TableColumns.Address1, holder.getAddress1());
        values.put(TableColumns.Address2, holder.getAddress2());
        values.put(TableColumns.AreaId, holder.getAreaId());
        values.put(TableColumns.Mobile, holder.getMobile());
//        values.put(TableColumns.DateAdded, holder.getDateAdded());
        values.put(TableColumns.DateModified, holder.getDateAdded());
        values.put(TableColumns.DeletedOn,"1" );
        db.update(TableNames.CUSTOMER, values, TableColumns.ID + " ='" + custId + "'", null);
    }

    public static void updatedeletedCustomerDetail(SQLiteDatabase db, String custId, String deletedDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DeletedOn, deletedDate);
//        values.put(TableColumns.SYNC_STATUS, "0");
        db.update(TableNames.CUSTOMER, values, TableColumns.ID + " ='" + custId + "'", null);
    }

//    public static boolean isDeletedCustomer(SQLiteDatabase db, String custId) {
//        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE (" + TableColumns.DeletedOn + " ='"
//                + "1' OR " + TableColumns.DeletedOn + " >'" + Constants.getCurrentDate() + "') AND " + TableColumns.ID + " ='" + custId + "'";
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        Boolean result = cursor.getCount() > 0;
//
//        cursor.close();
//        return result;
//    }

    public static boolean isAreaAssociated(SQLiteDatabase db, final int areaId) {
        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.AreaId + " ='"
                + areaId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }


    public static String getCustomerDeletionDate(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
        String date = "1";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return date;
    }

//    public static ArrayList<VCustomers> getAllCustomersToSync(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'";
//        ArrayList<VCustomers> list = new ArrayList<>();
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                VCustomers holder = new VCustomers();
//                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
//                    holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
//                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
//                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
//                    holder.setBalance_amount(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Address1)) != null)
//                    holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.Address1)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Address2)) != null)
//                    holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.Address2)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.AreaId)) != null)
//                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)) != null)
//                    holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)) != null)
//                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)) != null)
//                    holder.setQuantityModifiedDate(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_QUANTITY_MODIFIED)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)) != null)
//                    holder.setIs_deleted(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
//                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
//                list.add(holder);
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        return list;
//    }


//    public static int getTotalMilkQuantytyByDay(SQLiteDatabase db, String date) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.DATE_QUANTITY_MODIFIED + " ='" + date + "'";
//        int quantityTotal = 0;
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    quantityTotal = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY))) + quantityTotal;
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//
//        return quantityTotal;
//
//    }

//    public static float getTotalMilkQuantyty(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER;
//        float quantityTotal = 0;
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    quantityTotal = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY))) + quantityTotal;
//
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return quantityTotal;
//
//    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Dirty, "1");
//        values.put(TableColumns.SYNC_STATUS, "1");

//        db.update(TableNames.CUSTOMER, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
//                + " AND " + TableColumns.Dirty + " ='" + "0" + "'", null);
    }

//    public static float getTotalMilkQuantytyForCustomer(SQLiteDatabase db, final String custId) {
//        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
//        float quantityTotal = 0;
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    quantityTotal = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return quantityTotal;
//
//    }


    public static String getBalanceForCustomer(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.ID + " ='" + custId + "'";
        String balance = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.Balance)) != null)
                    balance = cursor.getString(cursor.getColumnIndex(TableColumns.Balance));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return balance;
    }


    public static ArrayList<String> getAllCustomersMobileNo(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER;
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static String getCustomerMobileNo(SQLiteDatabase db, int id) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.ID + " ='" + id + "'";
        String list = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                list = cursor.getString(cursor.getColumnIndex(TableColumns.Mobile));
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }
//    Check if customer deleted for selected date
//    public static boolean isDeletedCustomer(SQLiteDatabase db, String day,int custId) {
//        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE (" + TableColumns.DeletedOn + " ='"
//                + "1" + "' OR " +TableColumns.DeletedOn+" >'"+day+"')"+" AND " + TableColumns.ID + " ='" + custId + "'";
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        Boolean result = cursor.getCount() > 0;
//
//        cursor.close();
//        return result;
//    }
    public static ArrayList<CustomersSetting> getAllCustomersBySelectedDateAndArea(SQLiteDatabase db, int areaid, String date) {
        String selectquery = "";
        if (areaid==-1) {
            selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + " (" + TableColumns.DeletedOn + " ='"
                + "1' OR " + TableColumns.DeletedOn + " >'" + date+ "')" ;
        } else
            selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.AreaId + " ='" + areaid + "' AND (" + TableColumns.DeletedOn + " ='"
                    + "1' OR " + TableColumns.DeletedOn + " >'" + date+ "')" ;


        ArrayList<CustomersSetting> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomersSetting holder = CustomerSettingTableManagement.getAllCustomersByCustomerId(db, cursor.getInt(cursor.getColumnIndex(TableColumns.ID)), date);
                list.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return list;
    }

}
