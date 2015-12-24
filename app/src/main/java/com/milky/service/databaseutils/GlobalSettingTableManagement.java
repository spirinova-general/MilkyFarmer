package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VGlobalSettings;

import java.util.ArrayList;

/**
 * Created by Neha on 11/30/2015.
 */
public class GlobalSettingTableManagement {

    public static void insertGlobalSettingData(SQLiteDatabase db, VGlobalSettings holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");
        db.insert(TableNames.TABLE_GLOBAL_SETTINGS, null, values);
    }

    public static VGlobalSettings getGlobalSettingDetail(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        VGlobalSettings holder = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {

                holder = new VGlobalSettings();
               if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setDefaultRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
            }
                while (cursor.moveToNext()) ;

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }
    public static String getDefaultRate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                   rate = cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE));

            }
            while (cursor.moveToNext()) ;

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return rate;
    }
    public static String getDefaultTax(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    rate = cursor.getString(cursor.getColumnIndex(TableColumns.TAX));

            }
            while (cursor.moveToNext()) ;

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return rate;
    }


    public static void updateGlobalSettingData(SQLiteDatabase db, VGlobalSettings holder, String accountId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        values.put(TableColumns.TAX, holder.getTax());
        db.update(TableNames.TABLE_GLOBAL_SETTINGS, values, TableColumns.ACCOUNT_ID + " ='" + accountId+"'", null);
    }

}
