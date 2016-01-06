package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomersList;

import java.util.ArrayList;

/**
 * Created by Neha on 11/30/2015.
 */
public class BillTableManagement {
    public static void insertBillData(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IS_CLEARED, holder.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMade());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.IS_OUTSTANDING, "1");
        values.put(TableColumns.DIRTY, "1");

        db.insert(TableNames.TABLE_CUSTOMER_BILL, null, values);
    }

    public static void updateBillData(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.AREA_ID, holder.getAreaId());

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStart_date() + "'", null);
    }

    public static void updateCurrentDateData(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.AREA_ID, holder.getAreaId());

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStart_date() + "'", null);
    }

    public static void updateEndDate(SQLiteDatabase db, VCustomersList holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
//        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.END_DATE, updatedEndDate);

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.END_DATE + " ='" + enddate + "'", null);
    }

    public static boolean isHasAmount(SQLiteDatabase db, String custId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.PAYMENT_MADE + " !='0'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static float getPreviousBill(SQLiteDatabase db, final String custId, final String day, final double quantity) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        float amount = 0;

        if (cursor.moveToFirst()) {
            do {
                if(Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)))>0)
                amount = ((Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))))
                        / 100) * (float)quantity;
                else
                    amount= Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * (float)quantity;

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return amount;
    }
    public static float getTotalRate(SQLiteDatabase db, final String custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        float amount = 0;

        if (cursor.moveToFirst()) {
            do {

                    amount= Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return amount;
    }

    public static ArrayList<VCustomersList> getCustomersBill(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'" + " AND " + TableColumns.DIRTY + " ='0'";
        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("false");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
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

    public static ArrayList<VCustomersList> getCustomersBillToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'" + " AND " + TableColumns.DIRTY + " ='0'";
        ArrayList<VCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VCustomersList holder = new VCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("false");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
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

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "1" + "'", null);
    }

    public static void updateData(SQLiteDatabase db, VCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());

        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.IS_OUTSTANDING, "1");
        values.put(TableColumns.DIRTY, "0");

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'", null);
    }
}
