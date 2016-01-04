package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VAccount;
import com.milky.viewmodel.VAreaMapper;

import java.util.ArrayList;

/**
 * Created by Neha on 12/26/2015.
 */
public class Account {
    public static void insertAccountDetails(SQLiteDatabase db, VAccount holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FARMER_CODE, System.currentTimeMillis());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DIRTY, "1");

        db.insert(TableNames.TABLE_ACCOUNT, null, values);
    }

    public static String getDefaultRate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    rate = cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return rate;
    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_ACCOUNT, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }
    public static void updateAccountDetails(SQLiteDatabase db, VAccount holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FARMER_CODE, System.currentTimeMillis());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.DIRTY, "0");

        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    public static String getDefautTax(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    rate = cursor.getString(cursor.getColumnIndex(TableColumns.TAX));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return rate;
    }

    public static VAccount getAccountDetailsToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT+" WHERE "+ TableColumns.SYNC_STATUS+" ='0'";
        Cursor cursor = db.rawQuery(selectquery, null);
        VAccount holder = new VAccount();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));


            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }
    public static VAccount getAccountDetails(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(selectquery, null);
        VAccount holder = new VAccount();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));


            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }

}
