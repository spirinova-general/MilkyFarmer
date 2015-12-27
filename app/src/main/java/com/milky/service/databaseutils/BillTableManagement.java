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
        values.put(TableColumns.ADJUSTMENTS,"0");
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IS_CLEARED, holder.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMade());
        values.put(TableColumns.DATE_ADDED, holder.getDeliverydate());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.DIRTY, "0");

        db.insert(TableNames.TABLE_CUSTOMER_BILL, null, values);
    }

    public static boolean isHasAmount(SQLiteDatabase db, String custId) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.PAYMENT_MADE + " !='0'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static float getPreviousBill(SQLiteDatabase db, final String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        float amount = 0;

        if (cursor.moveToFirst()) {
            do {

                amount = ((Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.RATE))) * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))))
                        / 100) * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

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
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }
}
